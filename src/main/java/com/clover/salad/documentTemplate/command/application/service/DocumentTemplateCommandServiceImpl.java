package com.clover.salad.documentTemplate.command.application.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.service.FileStorageService;
import com.clover.salad.contract.document.entity.DocumentTemplate;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplatePatchRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadResponseDTO;
import com.clover.salad.documentTemplate.command.domain.repository.DocumentTemplateCommandRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentTemplateCommandServiceImpl implements DocumentTemplateCommandService {

	private final DocumentTemplateCommandRepository documentTemplateCommandRepository;
	private final FileStorageService fileStorageService;

	@Override
	@Transactional
	public DocumentTemplateUploadResponseDTO uploadDocumentTemplate(MultipartFile file,
		DocumentTemplateUploadRequestDTO dto) throws IOException {

		FileUploadEntity uploaded = fileStorageService.store(file, "계약서");

		DocumentTemplate entity = DocumentTemplate.builder()
			.name(dto.getName())
			.description(dto.getDescription())
			.version("v1.0")
			.createdAt(LocalDateTime.now())
			.isDeleted(false)
			.fileUpload(uploaded)
			.build();

		DocumentTemplate saved = documentTemplateCommandRepository.save(entity);

		return new DocumentTemplateUploadResponseDTO(saved.getId(), "템플릿 업로드 성공");
	}

	@Override
	@Transactional
	public void deleteDocumentTemplate(Integer id) {
		DocumentTemplate template = documentTemplateCommandRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 템플릿입니다."));

		template.setDeleted(true);
		template.setCreatedAt(LocalDateTime.now());
	}

	@Override
	@Transactional
	public void patchDocumentTemplate(Integer id, DocumentTemplatePatchRequestDTO dto) throws IOException {
		DocumentTemplate template = documentTemplateCommandRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 템플릿입니다."));

		String currentVersion = template.getVersion();
		String nextVersion = incrementVersion(currentVersion);
		template.setVersion(nextVersion);

		if (dto.getName() != null) {
			template.setName(dto.getName());
		}

		if (dto.getDescription() != null) {
			template.setDescription(dto.getDescription());
		}

		if (dto.getFile() != null && !dto.getFile().isEmpty()) {
			FileUploadEntity newFile = fileStorageService.store(dto.getFile(), "계약서");
			template.setFileUpload(newFile);
		}

		template.setCreatedAt(LocalDateTime.now());
	}

	private String incrementVersion(String version) {
		try {
			if (version == null || !version.startsWith("v1.")) return "v1.1";
			String num = version.substring(3); // "v1.2" → "2"
			int next = Integer.parseInt(num) + 1;
			return "v1." + next;
		} catch (Exception e) {
			return "v1.1";
		}
	}


}
