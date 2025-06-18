
package com.clover.salad.documentTemplate.query.service;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateDTO;
import com.clover.salad.documentTemplate.query.dto.DocumentTemplateSearchDTO;
import com.clover.salad.documentTemplate.query.mapper.DocumentTemplateQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentTemplateQueryServiceImpl implements DocumentTemplateQueryService {

	private final DocumentTemplateQueryMapper mapper;

	@Override
	public List<DocumentTemplateDTO> getAllTemplates() {
		return mapper.findAll();
	}

	@Override
	public DocumentTemplateDTO getTemplateById(int id) {
		return mapper.findById(id);
	}

	@Override
	public List<DocumentTemplateDTO> searchTemplates(DocumentTemplateSearchDTO documentTemplateSearchDTO) {
		return mapper.findByCondition(documentTemplateSearchDTO);
	}
}
