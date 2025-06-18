package com.clover.salad.documentTemplate.command.application.dto;

import org.springframework.web.multipart.MultipartFile;

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
public class DocumentTemplatePatchRequestDTO {
	private String name;
	private String version;
	private String description;
	private MultipartFile file;
}
