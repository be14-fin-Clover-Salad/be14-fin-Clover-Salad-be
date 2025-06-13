package com.clover.salad.documentTemplate.command.application.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.service.FileStorageService;
import com.clover.salad.contract.document.entity.DocumentTemplate;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadRequestDTO;
import com.clover.salad.documentTemplate.command.application.dto.DocumentTemplateUploadResponseDTO;
import com.clover.salad.contract.document.repository.DocumentTemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentTemplateCommandServiceImpl implements DocumentTemplateCommandService {

	private final DocumentTemplateRepository documentTemplateRepository;
	private final FileStorageService fileStorageService;

	@Override
	public DocumentTemplateUploadResponseDTO uploadDocumentTemplate(MultipartFile file,
		DocumentTemplateUploadRequestDTO dto) throws IOException {

		FileUploadEntity uploaded = fileStorageService.store(file, "계약서");

		DocumentTemplate entity = DocumentTemplate.builder()
			.name(dto.getName())
			.description(dto.getDescription())
			.version("1")
			.createdAt(LocalDateTime.now())
			.isDeleted(false)
			.fileUpload(uploaded)
			.build();

		DocumentTemplate saved = documentTemplateRepository.save(entity);

		return new DocumentTemplateUploadResponseDTO(saved.getId(), "템플릿 업로드 성공");
	}
}
