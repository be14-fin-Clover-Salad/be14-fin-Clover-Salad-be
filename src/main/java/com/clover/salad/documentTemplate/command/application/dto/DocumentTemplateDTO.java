package com.clover.salad.documentTemplate.command.application.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentTemplateDTO {
	private Integer id;
	private String name;
	private String description;
	private String uploadFilePath;
	private String version;
	private LocalDateTime createdAt;
}