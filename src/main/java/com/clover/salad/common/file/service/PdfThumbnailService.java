package com.clover.salad.common.file.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.ResponseInputStream;

@Service
@RequiredArgsConstructor
public class PdfThumbnailService {
	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	/*
	 * S3에 저장된 PDF의 전체 URL을 받아, URI로부터 key를 추출한 뒤 첫 페이지만 PNG 바이트 배열로 반환
	 */
	public byte[] generateFirstPageThumbnail(String pdfUrl) throws IOException {
		String key;
		try {
			URI uri = new URI(pdfUrl);
			// 예: uri.getPath() -> "/contract/uuid_파일명.pdf"
			key = uri.getPath().substring(1);  // leading '/' 제거
		} catch (URISyntaxException e) {
			throw new IOException("잘못된 PDF URL 형식: " + pdfUrl, e);
		}

		try (ResponseInputStream<GetObjectResponse> s3is = s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build());
			 PDDocument doc = PDDocument.load(s3is)) {

			PDFRenderer renderer = new PDFRenderer(doc);
			BufferedImage img = renderer.renderImageWithDPI(0, 100);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", baos);
			return baos.toByteArray();
		} catch (S3Exception e) {
			throw new IOException("S3에서 PDF를 가져오는 중 오류: key=" + key, e);
		}
	}

	/*
	 * 생성된 PNG 바이트 배열을 S3에 업로드, S3 키 반환
	 */
	public String uploadThumbnailToS3(String pdfUrl, byte[] thumbnailBytes) throws IOException {
		// thumbnailUrl 대신 thumbnailKey 를 먼저 추출
		String pdfKey;
		try {
			URI uri = new URI(pdfUrl);
			pdfKey = uri.getPath().substring(1);
		} catch (URISyntaxException e) {
			throw new IOException("잘못된 PDF URL 형식: " + pdfUrl, e);
		}

		String thumbKey = pdfKey.replaceAll("\\.pdf$", "") + "-thumb.png";

		try {
			PutObjectRequest putReq = PutObjectRequest.builder()
				.bucket(bucket)
				.key(thumbKey)
				.contentType("image/png")
				.contentLength((long) thumbnailBytes.length)
				.build();

			s3Client.putObject(putReq, RequestBody.fromBytes(thumbnailBytes));
			return thumbKey;
		} catch (S3Exception e) {
			throw new IOException("S3에 썸네일을 업로드하는 중 오류: key=" + thumbKey, e);
		}
	}
}
