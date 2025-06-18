package com.clover.salad.contract.command.service.parser;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.document.entity.DocumentOrigin;

public interface PdfContractParsingStrategy {
	ContractUploadRequestDTO parseAll(String fullText, String unused1, String unused2, DocumentOrigin origin);
}
