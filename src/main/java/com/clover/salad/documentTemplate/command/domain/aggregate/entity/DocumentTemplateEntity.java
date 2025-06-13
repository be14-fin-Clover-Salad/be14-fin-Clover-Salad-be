// package com.clover.salad.documentTemplate.command.domain.aggregate.entity;
//
// import java.time.LocalDateTime;
//
// import com.fasterxml.jackson.annotation.JsonProperty;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;
//
// @Entity
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @ToString
// @Builder
// @Table(name = "document_template")
// public class DocumentTemplateEntity {
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.IDENTITY)
// 	private int id;
//
// 	@Column(name = "name")
// 	private String name;
//
// 	@Column(name = "version")
// 	private String version;
//
// 	@Column(name = "description")
// 	private String description;
//
// 	@Column(name = "created_at")
// 	private LocalDateTime createdAt;
//
// 	@Column(name = "is_deleted")
// 	@JsonProperty("is_deleted")
// 	private boolean isDeleted;
//
// 	@Column(name = "file_upload_id")
// 	private int fileUploadId;
// }
