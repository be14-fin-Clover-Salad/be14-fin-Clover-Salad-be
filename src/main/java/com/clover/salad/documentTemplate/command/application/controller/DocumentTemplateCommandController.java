package com.clover.salad.documentTemplate.command.application.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplatePatchRequestDTO;
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
		@RequestPart("meta") DocumentTemplateUploadRequestDTO dto) {

		try {
			DocumentTemplateUploadResponseDTO response =
				documentTemplateCommandService.uploadDocumentTemplate(file, dto);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(
				new DocumentTemplateUploadResponseDTO(null, "템플릿 업로드 실패: " + e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTemplate(@PathVariable Integer id) {
		try {
			documentTemplateCommandService.deleteDocumentTemplate(id);
			return ResponseEntity.ok("템플릿 삭제 완료");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("삭제 실패: " + e.getMessage());
		}
	}

	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> patchTemplate(
		@PathVariable Integer id,
		@RequestPart("meta") DocumentTemplatePatchRequestDTO documentTemplatePatchRequestDTO) {
		try {
			documentTemplateCommandService.patchDocumentTemplate(id, documentTemplatePatchRequestDTO);
			return ResponseEntity.ok("템플릿 항목 수정 완료");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("수정 실패: " + e.getMessage());
		}
	}

}
