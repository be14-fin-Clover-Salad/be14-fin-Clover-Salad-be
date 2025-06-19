package com.clover.salad.product.command.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ProductDTO {
	private int id;
	private String category;
	private String name;
	private String serialNumber;
	private String productCode;
	private String company;
	private int originCost;
	private int rentalCost;
	private String description;
	@JsonProperty("isDeleted")
	private boolean isDeleted;
	private int fileUploadId;
	private String fileName;
	private String fileUrl;
}
