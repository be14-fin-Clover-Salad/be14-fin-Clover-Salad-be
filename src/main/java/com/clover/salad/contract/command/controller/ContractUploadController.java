package com.clover.salad.contract.command.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.contract.command.dto.ContractDeleteResponseDTO;
import com.clover.salad.contract.command.dto.ContractUpdateRequestDTO;
import com.clover.salad.contract.command.dto.ContractUpdateResponseDTO;
import com.clover.salad.contract.command.dto.ContractUploadResponseDTO;
import com.clover.salad.contract.command.facade.ContractUploadFacade;
import com.clover.salad.contract.command.service.ContractService;
import com.clover.salad.contract.command.service.parser.PdfContractParserService;
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
	private final ContractUploadFacade contractUploadFacade;


	/**
	 * 계약서 PDF 파일을 업로드하고 계약 + 고객 정보를 저장
	 */
	@PostMapping("/upload")
	public ResponseEntity<ContractUploadResponseDTO> uploadContract(@RequestParam("file") MultipartFile file) {
		try {
			ContractUploadResponseDTO response = contractUploadFacade.handleUpload(file);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(
				new ContractUploadResponseDTO(-1, "계약 등록 실패: " + e.getMessage()));
		}
	}


	@PatchMapping("/update/{contractId}")
	public ResponseEntity<ContractUpdateResponseDTO> updateEtcOnly(
		@PathVariable int contractId,
		@RequestBody ContractUpdateRequestDTO request
	) {
		return ResponseEntity.ok(contractService.updateEtcOnly(contractId, request.getEtc()));
	}

	@PatchMapping("/delete/{contractId}")
	public ResponseEntity<ContractDeleteResponseDTO> deleteContract(@PathVariable int contractId) {
		return ResponseEntity.ok(contractService.deleteContract(contractId));
	}


	/*
	* 기존 계약 계약서 잘못 적힌경우 재업로드
	* 기존 계약을 SoftDelete처리하고, 새 계약 등록한뒤, 이력 기록을 남김
	* */
	@PatchMapping("/replace/{contractId}")
	public ResponseEntity<ContractUploadResponseDTO> replaceContractPdf(
		@PathVariable int contractId,
		@RequestParam("file") MultipartFile file,
		@RequestParam(value = "note", required = false) String note
	) {
		try {
			ContractUploadResponseDTO response = contractUploadFacade.handleReplace(contractId, file, note);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(
				new ContractUploadResponseDTO(-1, "계약 갱신 실패: " + e.getMessage()));
		}
	}
}
