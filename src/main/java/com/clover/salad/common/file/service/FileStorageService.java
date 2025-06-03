package com.clover.salad.common.file.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.common.file.entity.FileUploadEntity;

public interface FileStorageService {
	FileUploadEntity store(MultipartFile file, String type) throws IOException;
}
