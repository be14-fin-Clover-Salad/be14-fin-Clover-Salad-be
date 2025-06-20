package com.clover.salad.common.file.controller;

import com.clover.salad.common.file.dto.FileUploadResultDTO;
import com.clover.salad.common.file.enums.FileUploadType;
import com.clover.salad.common.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/command/file")
@RequiredArgsConstructor
public class FileUploadController {

	private final FileUploadService fileUploadService;

	/**
	 * 공통 파일 업로드 엔드포인트
	 * @param file MultipartFile 객체
	 * @param type 파일 타입 (CONTRACT, PRODUCT, EMPLOYEE)
	 * 각 도메인에서 필요한 곳에서 의존성 주입해서 bean화 시켜서
	 * 사용하면 됩니다.
	 */
	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(
		@RequestParam("file") MultipartFile file,
		@RequestParam("type") FileUploadType type
	) {
		try {
			var entity = fileUploadService.uploadAndSave(file, type);
			var resultDTO = FileUploadResultDTO.builder()
				.id(entity.getId())
				.originFile(entity.getOriginFile())
				.renamedFile(entity.getRenameFile())
				.url(entity.getPath())
				.type(entity.getType())
				.build();

			return ResponseEntity.ok(resultDTO);
		} catch (Exception e) {
			log.error("[파일 업로드 실패]", e);
			return ResponseEntity.internalServerError().body("파일 업로드 실패: " + e.getMessage());
		}
	}
}
