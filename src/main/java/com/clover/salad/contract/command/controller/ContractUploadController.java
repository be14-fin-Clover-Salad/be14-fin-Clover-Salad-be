package com.clover.salad.contract.command.controller;

import java.io.File;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.contract.command.dto.ContractUpdateRequestDTO;
import com.clover.salad.contract.command.dto.ContractUpdateResponseDTO;
import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.dto.ContractUploadResponseDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.service.ContractService;
import com.clover.salad.contract.command.service.PdfContractParserService;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.service.DocumentOriginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/command/contract")
@RequiredArgsConstructor
public class ContractUploadController {

	private final PdfContractParserService pdfContractParserService;
	private final ContractService contractService;
	private final DocumentOriginService documentOriginService;

	/**
	 * 계약서 PDF 파일을 업로드하고 계약 + 고객 정보를 저장
	 */
	@PostMapping("/upload")
	public ResponseEntity<ContractUploadResponseDTO> uploadContract(@RequestParam("file") MultipartFile file) {
		try {
			// Step 1. 임시 파일 생성 및 MultipartFile 저장
			String originalFilename = file.getOriginalFilename();
			File tempFile = File.createTempFile("uploaded-contract", ".pdf");
			file.transferTo(tempFile);

			// Step 2. 임시 파일 → 최종 저장 (파일 이동) 및 DB 기록
			DocumentOrigin documentOrigin = documentOriginService.uploadAndSave(tempFile, originalFilename);
			File savedFile = new File(documentOrigin.getFileUpload().getPath());

			// Step 3. 저장된 실제 파일로 PDF 분석 + DocumentOrigin 포함
			ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(savedFile, documentOrigin);

			// Step 4. 계약 등록
			ContractEntity savedContract = contractService.registerContract(parsed);

			return ResponseEntity.ok(
				new ContractUploadResponseDTO(savedContract.getId(), "계약 등록 완료")
			);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(
				new ContractUploadResponseDTO(-1, "계약 등록 실패: " + e.getMessage())
			);
		}
	}

	@PatchMapping("/update/{contractId}")
	public ResponseEntity<ContractUpdateResponseDTO> updateEtcOnly(
		@PathVariable int contractId,
		@RequestBody ContractUpdateRequestDTO request
	) {
		return ResponseEntity.ok(contractService.updateEtcOnly(contractId, request.getEtc()));
	}

}
