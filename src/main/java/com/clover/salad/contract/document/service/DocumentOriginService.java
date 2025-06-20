package com.clover.salad.contract.document.service;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.enums.FileUploadType;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.entity.DocumentTemplate;
import com.clover.salad.contract.document.repository.DocumentOriginRepository;
import com.clover.salad.contract.document.repository.DocumentTemplateRepository;
import com.clover.salad.common.file.service.FileUploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentOriginService {

	private final FileUploadService fileUploadService;
	private final DocumentOriginRepository documentOriginRepository;
	private final FileUploadRepository fileUploadRepository;
	private final DocumentTemplateRepository documentTemplateRepository;

	@Transactional
	public DocumentOrigin uploadAndSave(File tempFile, String originalFilename) throws IOException {
		if (tempFile == null || !tempFile.exists()) {
			throw new IllegalArgumentException("임시 파일이 존재하지 않습니다.");
		}

		FileUploadEntity fileUpload = fileUploadService.uploadAndSave(tempFile, originalFilename, FileUploadType.CONTRACT);

		DocumentTemplate defaultTemplate = documentTemplateRepository.findById(1)
			.orElseThrow(() -> new IllegalStateException("기본 템플릿이 존재하지 않습니다."));

		DocumentOrigin documentOrigin = DocumentOrigin.builder()
			.fileUpload(fileUpload)
			.documentTemplate(defaultTemplate)
			.isDeleted(false)
			.createdAt(LocalDateTime.now())
			.build();

		return documentOriginRepository.save(documentOrigin);
	}

	@Transactional
	public void rollback(DocumentOrigin origin) {
		if (origin == null || origin.getFileUpload() == null) return;

		FileUploadEntity fileUpload = origin.getFileUpload();
		fileUploadService.deleteFromS3(fileUpload.getPath());

		documentOriginRepository.delete(origin);
		fileUploadRepository.delete(fileUpload);
	}
}
