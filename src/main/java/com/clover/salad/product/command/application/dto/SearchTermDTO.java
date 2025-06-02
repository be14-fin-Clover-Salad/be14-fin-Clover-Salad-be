package com.clover.salad.product.command.application.dto;

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
public class SearchTermDTO {
	private String productCode;
	private String category;
	private Integer minOriginCost;
	private Integer maxOriginCost;
	private Integer minRentalCost;
	private Integer maxRentalCost;
	private String name;
	private String serialNumber;
	private String company;
}
