package com.clover.salad.documentTemplate.query.dto;

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
public class DocumentTemplateSearchDTO {
	private String name;
	private String version;
	private String createdAtStart;
	private String createdAtEnd;
}
