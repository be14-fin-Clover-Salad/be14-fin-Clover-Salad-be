package com.clover.salad.product.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductImageDTO {
	private Integer fileUploadId;
	private String fileName;
	private String fileUrl;
}
