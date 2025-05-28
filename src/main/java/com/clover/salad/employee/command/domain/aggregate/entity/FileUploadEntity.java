package com.clover.salad.employee.command.domain.aggregate.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "file_upload")
public class FileUploadEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name="origin_file", nullable = false)
	private String originFile;

	@Column(name="rename_file", nullable = false)
	private String renameFile;

	@Column(name="path", nullable = false)
	private String path;

	@Column(name="created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name="type", nullable = false)
	private String type;
}
