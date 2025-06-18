package com.clover.salad.documentTemplate.command.application.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplatePatchRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadResponseDTO;

public interface DocumentTemplateCommandService {

	DocumentTemplateUploadResponseDTO uploadDocumentTemplate(MultipartFile file, DocumentTemplateUploadRequestDTO dto)
		throws IOException;

	void deleteDocumentTemplate(Integer id);

	void patchDocumentTemplate(Integer id, DocumentTemplatePatchRequestDTO dto) throws IOException;

}
