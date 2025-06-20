package com.clover.salad.contract.document.service;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.enums.FileUploadType;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.entity.DocumentTemplate;
import com.clover.salad.contract.document.repository.DocumentOriginRepository;
import com.clover.salad.contract.document.repository.DocumentTemplateRepository;
import com.clover.salad.common.file.service.S3Uploader;
import com.clover.salad.common.file.service.S3PathResolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentOriginService {

	private final FileUploadRepository fileUploadRepository;
	private final DocumentOriginRepository documentOriginRepository;
	private final DocumentTemplateRepository documentTemplateRepository;
	private final S3Uploader s3Uploader;
	private final S3PathResolver s3PathResolver;

	@Transactional
	public DocumentOrigin uploadAndSave(File tempFile, String originalFilename) throws IOException {
		if (tempFile == null || !tempFile.exists()) {
			throw new IllegalArgumentException("임시 파일이 존재하지 않습니다.");
		}

		String renamedFile = UUID.randomUUID() + "_" + originalFilename;
		String s3Key = s3PathResolver.resolve(FileUploadType.CONTRACT, renamedFile);
		String s3Path = s3Uploader.upload(tempFile, s3Key);

		FileUploadEntity fileUpload = fileUploadRepository.save(FileUploadEntity.builder()
			.originFile(originalFilename)
			.renameFile(renamedFile)
			.path(s3Key) // 실제 업로드된 S3 키 저장
			.type(FileUploadType.CONTRACT)
			.build());

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

		try {
			String s3Key = fileUpload.getPath();
			s3Uploader.delete(s3Key); // S3 경로 삭제
		} catch (Exception e) {
			System.err.println("S3 삭제 실패: " + e.getMessage());
		}

		documentOriginRepository.delete(origin);
		fileUploadRepository.delete(fileUpload);
	}
}
