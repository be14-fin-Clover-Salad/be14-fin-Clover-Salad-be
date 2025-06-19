package com.clover.salad.common.file.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.enums.FileUploadType;
import com.clover.salad.common.file.repository.FileUploadRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileUploadService {

	private final S3Uploader s3Uploader;
	private final S3PathResolver pathResolver;
	private final FileUploadRepository fileUploadRepository;

	public FileUploadEntity uploadAndSave(MultipartFile multipartFile, FileUploadType type) throws IOException {
		String origin = multipartFile.getOriginalFilename();
		String renamed = UUID.randomUUID() + "_" + origin;
		File tempFile = File.createTempFile("upload-", ".tmp");
		multipartFile.transferTo(tempFile);

		String key = pathResolver.resolve(type, renamed);
		String url = s3Uploader.upload(tempFile, key);

		FileUploadEntity entity = FileUploadEntity.builder()
			.originFile(origin)
			.renameFile(renamed)
			.path(url)
			.type(type)
			.build();

		return fileUploadRepository.save(entity);
	}
}
