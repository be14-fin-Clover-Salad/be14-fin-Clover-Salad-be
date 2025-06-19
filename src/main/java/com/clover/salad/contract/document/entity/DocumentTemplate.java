package com.clover.salad.contract.document.entity;

import com.clover.salad.common.file.entity.FileUploadEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String version = "v1.0";

	private String description;

	private LocalDateTime createdAt;

	@JsonProperty("isDeleted")
	private boolean isDeleted;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_upload_id", nullable = false)
	private FileUploadEntity fileUpload;
}
