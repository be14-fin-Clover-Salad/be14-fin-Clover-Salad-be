package com.clover.salad.documentTemplate.query.controller;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateDTO;
import com.clover.salad.documentTemplate.query.dto.DocumentTemplateSearchDTO;
import com.clover.salad.documentTemplate.query.service.DocumentTemplateQueryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/query/documentTemplate")
@RequiredArgsConstructor
public class DocumentTemplateQueryController {

	private final DocumentTemplateQueryService service;

	@GetMapping
	public List<DocumentTemplateDTO> getAllTemplates() {
		return service.getAllTemplates();
	}

	@GetMapping("/{id}")
	public DocumentTemplateDTO getTemplate(@PathVariable int id) {
		return service.getTemplateById(id);
	}

	@PostMapping("/search")
	public ResponseEntity<List<DocumentTemplateDTO>> searchTemplates(@RequestBody DocumentTemplateSearchDTO dto) {
		return ResponseEntity.ok(service.searchTemplates(dto));
	}
}
