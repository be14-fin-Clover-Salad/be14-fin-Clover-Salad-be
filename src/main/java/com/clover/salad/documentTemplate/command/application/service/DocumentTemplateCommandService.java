package com.clover.salad.documentTemplate.command.application.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadResponseDTO;

public interface DocumentTemplateCommandService {

	DocumentTemplateUploadResponseDTO uploadDocumentTemplate(MultipartFile file, DocumentTemplateUploadRequestDTO dto) throws
		IOException;

}
