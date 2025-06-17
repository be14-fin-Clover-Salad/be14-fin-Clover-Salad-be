package com.clover.salad.documentTemplate.query.service;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateDTO;
import com.clover.salad.documentTemplate.query.mapper.DocumentTemplateQueryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTemplateQueryServiceImpl implements DocumentTemplateQueryService {

	private final DocumentTemplateQueryMapper documentTemplateQueryMapper;

	public DocumentTemplateQueryServiceImpl(DocumentTemplateQueryMapper documentTemplateQueryMapper) {
		this.documentTemplateQueryMapper = documentTemplateQueryMapper;
	}

	@Override
	public List<DocumentTemplateDTO> getAllTemplates() {
		return documentTemplateQueryMapper.findAll();
	}

	@Override
	public DocumentTemplateDTO getTemplateById(int id) {
		return documentTemplateQueryMapper.findById(id);
	}
}
