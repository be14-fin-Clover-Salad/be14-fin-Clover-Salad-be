package com.clover.salad.common.file.entity;

import java.time.LocalDateTime;

import com.clover.salad.common.file.enums.FileUploadType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FILE_UPLOAD")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileUploadEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "origin_file", nullable = false, length = 512)
	private String originFile;

	@Column(name = "rename_file", nullable = false, length = 512)
	private String renameFile;

	@Column(name = "path", nullable = false, length = 512)
	private String path;

	@Column(name = "thumbnail_path", length = 512)
    private String thumbnailPath;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 10)
	private FileUploadType type;

	@Builder
	public FileUploadEntity(String originFile, String renameFile, String path, FileUploadType type) {
		this.originFile = originFile;
		this.renameFile = renameFile;
		this.path = path;
		this.createdAt = LocalDateTime.now();
		this.type = type;
	}
}
