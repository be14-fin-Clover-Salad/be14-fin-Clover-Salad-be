package com.clover.salad.common.file.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.enums.FileUploadType;
import com.clover.salad.common.file.repository.FileUploadRepository;

@Service
public class LocalFileStorageService implements FileStorageService {

	@Value("${file.upload.dir}")
	private String uploadDir;

	private final FileUploadRepository fileUploadRepository;

	public LocalFileStorageService(FileUploadRepository fileUploadRepository) {
		this.fileUploadRepository = fileUploadRepository;
	}

	public FileUploadEntity store(MultipartFile file, String type) throws IOException {
		// 1. rename
		String rename = UUID.randomUUID() + "_" + file.getOriginalFilename();

		// 2. 저장
		Path path = Paths.get(uploadDir + "/" + rename);
		file.transferTo(path.toFile());

		// 3. DB 저장
		FileUploadEntity upload = FileUploadEntity.builder()
			.originFile(file.getOriginalFilename())
			.renameFile(rename)
			.path(uploadDir)
			.type(FileUploadType.valueOf(type))
			.build();

		return fileUploadRepository.save(upload);
	}
}
