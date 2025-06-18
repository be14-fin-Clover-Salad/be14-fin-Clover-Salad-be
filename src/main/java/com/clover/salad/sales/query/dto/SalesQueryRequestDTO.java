package com.clover.salad.sales.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SalesQueryRequestDTO {
	private String startDate;
	private String endDate;
	private String department;
	private Integer minAmount;
	private Integer maxAmount;
	private String employeeName;
	private String contractCode;
}
