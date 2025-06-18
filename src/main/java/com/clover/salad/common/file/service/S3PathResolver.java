package com.clover.salad.common.file.service;

import org.springframework.stereotype.Component;

import com.clover.salad.common.file.enums.FileUploadType;

@Component
public class S3PathResolver {

	public String resolve(FileUploadType type, String renamedFile) {
		String prefix;
		switch (type) {
			case CONTRACT -> prefix = "contract/";
			case PRODUCT -> prefix = "product/";
			case EMPLOYEE -> prefix = "employee/";
			default -> throw new IllegalArgumentException("Unknown file type");
		}
		return prefix + renamedFile;
	}
}

