package com.clover.salad.contract.command.facade;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.dto.ContractUploadResponseDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.entity.ContractFileHistory;
import com.clover.salad.contract.command.service.ContractService;
import com.clover.salad.contract.command.service.parser.PdfContractParserService;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.service.DocumentOriginService;
import com.clover.salad.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
		File tempFile = File.createTempFile("uploaded-contract", ".pdf");
		file.transferTo(tempFile);

		DocumentOrigin documentOrigin = documentOriginService.uploadAndSave(tempFile, originalFilename);
		File savedFile = new File(documentOrigin.getFileUpload().getPath());

		ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(savedFile, documentOrigin);
		ContractEntity saved = contractService.registerContract(parsed);

		return new ContractUploadResponseDTO(saved.getId(), "계약 등록 완료");
	}

	/**
	 * 기존 계약 PDF 재업로드 (버전 히스토리 포함)
	 */
	public ContractUploadResponseDTO handleReplace(int contractId, MultipartFile file, String note) throws Exception {
		log.info("== [1] 계약 ID: {} 파일명: {} ==>", contractId, file.getOriginalFilename());

		File tempFile = File.createTempFile("uploaded-replace", ".pdf");
		file.transferTo(tempFile);
		log.info("== [2] 임시 파일 저장 완료: {} ==", tempFile.getAbsolutePath());

		DocumentOrigin documentOrigin = null;
		try {
			log.info("== [3] 기존 계약 무효 처리 시작 ==");
			ContractEntity existingContract = contractService.markContractDeleted(contractId);
			log.info("== [3-1] 기존 계약 무효 처리 완료: id={}, status={}, deleted={}",
				existingContract.getId(), existingContract.getStatus(), existingContract.isDeleted());

			log.info("== [4] 파일 영구 저장 시작 ==");
			documentOrigin = documentOriginService.uploadAndSave(tempFile, file.getOriginalFilename());
			File savedFile = new File(documentOrigin.getFileUpload().getPath());
			log.info("== [4-1] 영구 저장 완료: {}", savedFile.getAbsolutePath());

			log.info("== [5] PDF 파싱 시작 ==");
			ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(savedFile, documentOrigin);
			log.info("== [5-1] 파싱 결과: 고객={}, 계약={}, 상품개수={}",
				parsed.getCustomer().getName(), parsed.getContract().getStartDate(), parsed.getProducts().size());

			log.info("== [6] 신규 계약 등록 시작 ==");
			ContractEntity newContract = contractService.registerContract(parsed);
			log.info("== [6-1] 신규 계약 등록 완료: id={}", newContract.getId());

			log.info("== [7] 히스토리 저장 시작 ==");
			ContractFileHistory history = ContractFileHistory.builder()
				.contract(existingContract)
				.version(contractService.getNextVersion(contractId))
				.originFile(file.getOriginalFilename())
				.renamedFile(documentOrigin.getFileUpload().getRenameFile())
				.uploaderId(SecurityUtil.getEmployeeId())
				.note(note)
				.uploadedAt(LocalDateTime.now())
				.build();

			contractService.saveContractHistory(history);
			log.info("== [7-1] 히스토리 저장 완료: version={}", history.getVersion());

			return new ContractUploadResponseDTO(newContract.getId(), "기존 계약 갱신 완료");

		} finally {
			if (tempFile.exists()) {
				boolean deleted = tempFile.delete();
				log.info("== [FINALLY] 임시 파일 삭제: {}, 성공 여부={}", tempFile.getAbsolutePath(), deleted);
			}
		}
	}


}
