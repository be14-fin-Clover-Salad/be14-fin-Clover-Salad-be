package com.clover.salad.contract.command.facade;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.dto.ContractUploadResponseDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.entity.ContractFileHistory;
import com.clover.salad.contract.command.service.ContractService;
import com.clover.salad.contract.command.service.parser.PdfContractParserService;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.service.DocumentOriginService;
import com.clover.salad.contract.exception.ContractUploadFailedException;
import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.common.file.service.PdfThumbnailService;
import com.clover.salad.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractUploadFacade {

	private final PdfContractParserService pdfContractParserService;
	private final ContractService contractService;
	private final DocumentOriginService documentOriginService;
	private final PdfThumbnailService thumbnailService;
	private final FileUploadRepository fileUploadRepository;

	/**
	 * 신규 계약 업로드
	 */
	public ContractUploadResponseDTO handleUpload(MultipartFile file) throws Exception {
		String originalFilename = file.getOriginalFilename();
		Path tempPath = Files.createTempFile("uploaded-contract", ".pdf");
		File tempFile = tempPath.toFile();
		file.transferTo(tempFile);

		DocumentOrigin documentOrigin = null;
		try {
			// 1. 파싱 및 유효성 검증
			ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(tempFile);
			contractService.validate(parsed);

			// 2. PDF S3 업로드 및 DocumentOrigin 저장
			documentOrigin = documentOriginService.uploadAndSave(tempFile, originalFilename);
			parsed.setDocumentOrigin(documentOrigin);

			// 3. 썸네일 생성 및 S3 업로드
			String pdfKey = documentOrigin.getFileUpload().getPath();
			byte[] thumbBytes = thumbnailService.generateFirstPageThumbnail(pdfKey);
			String thumbKey = thumbnailService.uploadThumbnailToS3(pdfKey, thumbBytes);

			// 4. 썸네일 경로 DB 반영
			FileUploadEntity pdfUpload = documentOrigin.getFileUpload();
			pdfUpload.setThumbnailPath(thumbKey);
			fileUploadRepository.save(pdfUpload);

			// 5. 계약 등록
			ContractEntity saved = contractService.registerContract(parsed);
			return new ContractUploadResponseDTO(saved.getId(), "계약 등록 완료");

		} catch (Exception e) {
			log.error("[ERROR] 계약 업로드 중 예외 발생: {}", e.getMessage(), e);
			if (documentOrigin != null) {
				try {
					documentOriginService.rollback(documentOrigin);
					log.warn("[ROLLBACK] 문서 원본 롤백 처리 완료");
				} catch (Exception rollbackEx) {
					log.error("[ROLLBACK ERROR] 롤백 중 예외 발생: {}", rollbackEx.getMessage(), rollbackEx);
				}
			}
			throw new ContractUploadFailedException("계약 등록 실패", e);
		} finally {
			Files.deleteIfExists(tempPath);
			log.info("[CLEANUP] 임시 파일 삭제 완료: {}", tempPath);
		}
	}

	/*
	 * 기존 계약 PDF 재업로드
	 */
	@Transactional
	public ContractUploadResponseDTO handleReplace(int contractId, MultipartFile file, String note) throws Exception {
		Path tempPath = Files.createTempFile("uploaded-replace", ".pdf");
		File tempFile = tempPath.toFile();
		file.transferTo(tempFile);

		DocumentOrigin documentOrigin = null;
		try {
			// 1. 기존 계약 무효 처리
			ContractEntity existingContract = contractService.markContractDeleted(contractId);

			// 2. 파싱 및 검증
			ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(tempFile);
			contractService.validate(parsed);

			// 3. PDF S3 업로드 및 DocumentOrigin 저장
			documentOrigin = documentOriginService.uploadAndSave(tempFile, file.getOriginalFilename());
			parsed.setDocumentOrigin(documentOrigin);

			// 4. 썸네일 생성 및 S3 업로드
			String pdfKey = documentOrigin.getFileUpload().getPath();
			byte[] thumbBytes = thumbnailService.generateFirstPageThumbnail(pdfKey);
			String thumbKey = thumbnailService.uploadThumbnailToS3(pdfKey, thumbBytes);

			// 5. 썸네일 경로 DB 반영
			FileUploadEntity pdfUpload = documentOrigin.getFileUpload();
			pdfUpload.setThumbnailPath(thumbKey);
			fileUploadRepository.save(pdfUpload);

			// 6. 신규 계약 등록
			ContractEntity newContract = contractService.registerContract(parsed);
			if (newContract == null || newContract.getId() <= 0) {
				throw new IllegalStateException("신규 계약 등록 실패: 유효한 계약 ID가 생성되지 않음");
			}

			// 7. 버전 히스토리 저장
			ContractFileHistory history = ContractFileHistory.builder()
				.contract(newContract)
				.replacedContract(existingContract)
				.version(contractService.getNextVersion(existingContract.getId()))
				.originFile(file.getOriginalFilename())
				.renamedFile(documentOrigin.getFileUpload().getRenameFile())
				.uploaderId(SecurityUtil.getEmployeeId())
				.note(note)
				.uploadedAt(LocalDateTime.now())
				.build();
			contractService.saveContractHistory(history);

			return new ContractUploadResponseDTO(newContract.getId(), "기존 계약 갱신 완료");

		} catch (Exception e) {
			log.error("[ERROR] 계약 재업로드 중 예외 발생: {}", e.getMessage(), e);
			if (documentOrigin != null) {
				try {
					documentOriginService.rollback(documentOrigin);
					log.warn("[ROLLBACK] 문서 원본 롤백 처리 완료");
				} catch (Exception rollbackEx) {
					log.error("[ROLLBACK ERROR] 롤백 중 예외 발생: {}", rollbackEx.getMessage(), rollbackEx);
				}
			}
			throw new ContractUploadFailedException("계약 재업로드 실패", e);
		} finally {
			Files.deleteIfExists(tempPath);
			log.info("[CLEANUP] 임시 파일 삭제 완료: {}", tempPath);
		}
	}
}
