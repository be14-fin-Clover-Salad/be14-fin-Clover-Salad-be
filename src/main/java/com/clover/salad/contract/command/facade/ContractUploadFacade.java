package com.clover.salad.contract.command.facade;


import com.clover.salad.contract.command.dto.ContractUploadResponseDTO;
import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.service.ContractService;
import com.clover.salad.contract.command.service.parser.PdfContractParserService;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.service.DocumentOriginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ContractUploadFacade {

	private final PdfContractParserService pdfContractParserService;
	private final ContractService contractService;
	private final DocumentOriginService documentOriginService;

	public ContractUploadResponseDTO handleUpload(MultipartFile file) throws Exception {
		// 1. 임시 파일 저장
		String originalFilename = file.getOriginalFilename();
		File tempFile = File.createTempFile("uploaded-contract", ".pdf");
		file.transferTo(tempFile);

		// 2. 파일 영구 저장 및 문서 원본 엔티티 생성
		DocumentOrigin documentOrigin = documentOriginService.uploadAndSave(tempFile, originalFilename);
		File savedFile = new File(documentOrigin.getFileUpload().getPath());

		// 3. PDF 파싱
		ContractUploadRequestDTO parsed = pdfContractParserService.parsePdf(savedFile, documentOrigin);

		// 4. 계약 등록
		ContractEntity saved = contractService.registerContract(parsed);

		return new ContractUploadResponseDTO(saved.getId(), "계약 등록 완료");
	}
}
