package com.clover.salad.documentTemplate.command.application.dto;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentTemplateUploadResponseDTO {
	private Integer id;
	private String message;
}

