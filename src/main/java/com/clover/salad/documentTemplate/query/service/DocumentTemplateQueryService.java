// DocumentTemplateQueryService.java
package com.clover.salad.documentTemplate.query.service;

import java.util.List;

import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateDTO;
import com.clover.salad.documentTemplate.query.dto.DocumentTemplateSearchDTO;

public interface DocumentTemplateQueryService {
	List<DocumentTemplateDTO> getAllTemplates();

	DocumentTemplateDTO getTemplateById(int id);

	List<DocumentTemplateDTO> searchTemplates(DocumentTemplateSearchDTO dto);
}
