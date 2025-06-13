package com.clover.salad.documentTemplate.command.application.service;

import org.springframework.stereotype.Service;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadResponseDTO;


public interface DocumentTemplateCommandService {

	DocumentTemplateUploadResponseDTO uploadDocumentTemplate(DocumentTemplateUploadRequestDTO requestDTO);
}
