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
	private Long id;

	private LocalDateTime createdAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_upload_id")
	private FileUploadEntity fileUpload;

	private boolean isDeleted;


}

