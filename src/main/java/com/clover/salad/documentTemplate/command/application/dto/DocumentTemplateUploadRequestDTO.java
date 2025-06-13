package com.clover.salad.documentTemplate.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentTemplateUploadRequestDTO {
	private String name;
	private String description;
	private int fileUploadId;
}