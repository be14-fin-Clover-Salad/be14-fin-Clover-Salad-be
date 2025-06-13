// package com.clover.salad.documentTemplate.command.application.controller;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.clover.salad.contract.command.dto.ContractUploadResponseDTO;
// import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadRequestDTO;
// import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadResponseDTO;
// import com.clover.salad.documentTemplate.command.application.service.DocumentTemplateCommandService;
//
// @RestController
// @RequestMapping("/api/command/documentTemplate")
// public class DocumentTemplateCommandController {
//
// 	private final DocumentTemplateCommandService documentTemplateCommandService;
//
// 	@PostMapping("/upload")
// 	public ResponseEntity<DocumentTemplateUploadResponseDTO> uploadDocumentTemplate(
// 		@RequestBody DocumentTemplateUploadRequestDTO documentTemplateUploadRequestDTO) {
//
// 		try {
// 			DocumentTemplateUploadResponseDTO response =
// 				documentTemplateCommandService.uploadDocumentTemplate(documentTemplateUploadRequestDTO);
// 			return ResponseEntity.ok(response);
// 		} catch (Exception e) {
// 			return ResponseEntity.badRequest().body(
// 				new DocumentTemplateUploadResponseDTO(null, "템플릿 업로드 실패: " + e.getMessage()));
// 		}
// 	}
// }
