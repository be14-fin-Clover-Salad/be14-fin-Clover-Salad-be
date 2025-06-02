package com.clover.salad.contract.document.service;

import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.repository.DocumentOriginRepository;

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

	@Value("${file.upload.dir}")
	private String uploadDir; // C:/contracttest 로 주입

	@Transactional
	public DocumentOrigin uploadAndSave(File tempFile, String originalFilename) throws IOException {
		if (tempFile == null || !tempFile.exists()) {
			throw new IllegalArgumentException("임시 파일이 존재하지 않습니다.");
		}

		// 1. 저장 디렉토리 생성
		File saveDir = new File(uploadDir); // C:/contracttest
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		// 2. 저장 파일명
		String storedFileName = UUID.randomUUID() + "_" + originalFilename;
		File savedFile = new File(saveDir, storedFileName);

		// 3. 파일 이동 (복사 후 삭제)
		Files.copy(tempFile.toPath(), savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		tempFile.delete(); // 임시 파일 삭제

		// 4. 파일 DB 기록
		FileUploadEntity fileUpload = fileUploadRepository.save(FileUploadEntity.builder()
			.originFile(originalFilename)
			.renameFile(storedFileName)
			.path(savedFile.getAbsolutePath())
			.createdAt(LocalDateTime.now())
			.type("계약서")
			.build());

		// 5. 문서 기록
		DocumentOrigin documentOrigin = DocumentOrigin.builder()
			.fileUpload(fileUpload)
			.isDeleted(false)
			.createdAt(LocalDateTime.now())
			.build();

		documentOriginRepository.save(documentOrigin);

		return documentOrigin;
	}
}
