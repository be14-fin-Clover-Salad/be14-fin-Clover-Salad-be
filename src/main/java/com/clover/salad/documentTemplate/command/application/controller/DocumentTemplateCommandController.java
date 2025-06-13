package com.clover.salad.documentTemplate.command.application.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadResponseDTO;
import com.clover.salad.documentTemplate.command.application.service.DocumentTemplateCommandService;

@RestController
@RequestMapping("/api/command/documentTemplate")
public class DocumentTemplateCommandController {

	private final DocumentTemplateCommandService documentTemplateCommandService;

	public DocumentTemplateCommandController(DocumentTemplateCommandService documentTemplateCommandService) {
		this.documentTemplateCommandService = documentTemplateCommandService;
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DocumentTemplateUploadResponseDTO> uploadDocumentTemplate(
		@RequestPart("file") MultipartFile file,
		@RequestPart("meta") DocumentTemplateUploadRequestDTO documentTemplateUploadRequestDTO) {

		try {
			DocumentTemplateUploadResponseDTO response =
				documentTemplateCommandService.uploadDocumentTemplate(file, documentTemplateUploadRequestDTO);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(
				new DocumentTemplateUploadResponseDTO(null, "템플릿 업로드 실패: " + e.getMessage()));
		}
	}
}
