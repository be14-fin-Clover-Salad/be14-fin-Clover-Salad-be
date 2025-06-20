package com.clover.salad.common.file.dto;

import com.clover.salad.common.file.enums.FileUploadType;

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
public class FileUploadResultDTO {
	private Integer id;
	private String originFile;
	private String renamedFile;
	private String url;
	private FileUploadType type;
}



