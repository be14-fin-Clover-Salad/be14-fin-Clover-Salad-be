package com.clover.salad.common.file.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final S3Client s3Client;

	public String upload(File file, String key) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.build();

		try {
			s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
			return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build()).toString();
		} catch (S3Exception e) {
			log.error("S3 업로드 실패: {}", e.getMessage(), e);
			throw new RuntimeException("파일 업로드 실패", e);
		}
	}
}
