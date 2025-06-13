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
			documentOrigin = documentOriginService.uploadAndSave(tempFile, originalFilename);
			File savedFile = new File(documentOrigin.getFileUpload().getPath());

			ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(savedFile, documentOrigin);
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

	/**
	 * 기존 계약 PDF 재업로드 (버전 히스토리 포함)
	 */
	@Transactional
	public ContractUploadResponseDTO handleReplace(int contractId, MultipartFile file, String note) throws Exception {
		Path tempPath = Files.createTempFile("uploaded-replace", ".pdf");
		File tempFile = tempPath.toFile();
		file.transferTo(tempFile);

		DocumentOrigin documentOrigin = null;

		try {
			return processReupload(contractId, file, note, tempFile);
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

	private ContractUploadResponseDTO processReupload(int contractId, MultipartFile file, String note, File tempFile) throws Exception {
		log.info("== [1] 계약 ID: {} / 파일명: {} ==", contractId, file.getOriginalFilename());

		ContractEntity existingContract = contractService.markContractDeleted(contractId);
		log.info("== [2] 기존 계약 무효 처리 완료: id={}, status={}, deleted={}",
			existingContract.getId(), existingContract.getStatus(), existingContract.isDeleted());

		DocumentOrigin documentOrigin = documentOriginService.uploadAndSave(tempFile, file.getOriginalFilename());
		File savedFile = new File(documentOrigin.getFileUpload().getPath());

		log.info("== [3] PDF 파싱 시작 ==");
		ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(savedFile, documentOrigin);
		log.info("== [3-1] 파싱 결과: 고객={}, 계약시작일={}, 상품개수={}",
			parsed.getCustomer().getName(), parsed.getContract().getStartDate(), parsed.getProducts().size());

		log.info("== [4] 신규 계약 등록 시작 ==");
		ContractEntity newContract = contractService.registerContract(parsed);

		if (newContract == null || newContract.getId() <= 0) {
			throw new IllegalStateException("신규 계약 등록 실패: 유효한 계약 ID가 생성되지 않음");
		}

		log.info("== [4-1] 신규 계약 등록 완료: id={}", newContract.getId());

		log.info("== [5] 히스토리 저장 시작 ==");
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
		log.info("== [5-1] 히스토리 저장 완료: version={}", history.getVersion());

		return new ContractUploadResponseDTO(newContract.getId(), "기존 계약 갱신 완료");
	}
}
