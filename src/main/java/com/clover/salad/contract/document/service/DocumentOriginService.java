package com.clover.salad.contract.document.service;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.entity.DocumentTemplate;
import com.clover.salad.contract.document.repository.DocumentOriginRepository;
import com.clover.salad.contract.document.repository.DocumentTemplateRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentOriginService {

	private final FileUploadRepository fileUploadRepository;
	private final DocumentOriginRepository documentOriginRepository;
	private final DocumentTemplateRepository documentTemplateRepository;

	@Value("${file.upload.dir}")
	private String uploadDir;

	@Transactional
	public DocumentOrigin uploadAndSave(File tempFile, String originalFilename) throws IOException {
		if (tempFile == null || !tempFile.exists()) {
			throw new IllegalArgumentException("임시 파일이 존재하지 않습니다.");
		}

		// 1. 저장 디렉토리 생성
		File saveDir = new File(uploadDir);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		// 2. 저장 파일명
		String storedFileName = UUID.randomUUID() + "_" + originalFilename;
		File savedFile = new File(saveDir, storedFileName);

		// 3. 파일 이동
		Files.copy(tempFile.toPath(), savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		tempFile.delete();

		// 4. 파일 업로드 정보 저장
		FileUploadEntity fileUpload = fileUploadRepository.save(FileUploadEntity.builder()
			.originFile(originalFilename)
			.renameFile(storedFileName)
			.path(savedFile.getAbsolutePath())
			.createdAt(LocalDateTime.now())
			.type("계약서")
			.build());

		// 5. 기본 템플릿 조회
		DocumentTemplate defaultTemplate = documentTemplateRepository.findById(1)
			.orElseThrow(() -> new IllegalStateException("기본 템플릿이 존재하지 않습니다."));

		// 6. 문서 원본 저장
		DocumentOrigin documentOrigin = DocumentOrigin.builder()
			.fileUpload(fileUpload)
			.documentTemplate(defaultTemplate)
			.isDeleted(false)
			.createdAt(LocalDateTime.now())
			.build();

		return documentOriginRepository.save(documentOrigin);
	}
}
