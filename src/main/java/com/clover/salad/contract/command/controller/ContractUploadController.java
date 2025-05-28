package com.clover.salad.contract.command.controller;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.dto.ContractUploadResponseDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.entity.CustomerEntity;
import com.clover.salad.contract.command.repository.ContractRepository;
import com.clover.salad.contract.command.repository.CustomerRepository;
import com.clover.salad.contract.command.service.PdfContractParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractUploadController {

	private final PdfContractParserService parserService;
	private final CustomerRepository customerRepository;
	private final ContractRepository contractRepository;

	/**
	 * PDF 업로드 및 고객+계약 저장
	 */
	@PostMapping("/upload")
	public ResponseEntity<ContractUploadResponseDTO> upload(@RequestParam("file") MultipartFile file) {
		try {
			File tempFile = File.createTempFile("contract-", ".pdf");
			file.transferTo(tempFile);

			ContractUploadRequestDTO parsed = parserService.parsePdf(tempFile);

			CustomerEntity savedCustomer = customerRepository.save(parsed.getCustomer().toEntity());
			ContractEntity contract = parsed.getContract().toEntity(savedCustomer);
			contractRepository.save(contract);

			return ResponseEntity.ok(new ContractUploadResponseDTO(contract.getId(), "저장 완료"));

		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.body(new ContractUploadResponseDTO(-1, "실패: " + e.getMessage()));
		}
	}
}
