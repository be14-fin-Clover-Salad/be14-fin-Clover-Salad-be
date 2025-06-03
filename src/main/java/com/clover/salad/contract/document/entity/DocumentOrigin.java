package com.clover.salad.contract.document.entity;

import java.time.LocalDateTime;

import com.clover.salad.common.file.entity.FileUploadEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_origin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentOrigin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private LocalDateTime createdAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_upload_id", nullable = false)
	private FileUploadEntity fileUpload;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "document_template_id", nullable = false)
	private DocumentTemplate documentTemplate;

	private boolean isDeleted;
}

