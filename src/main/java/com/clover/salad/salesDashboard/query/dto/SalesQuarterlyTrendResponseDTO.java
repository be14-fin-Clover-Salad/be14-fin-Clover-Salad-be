package com.clover.salad.salesDashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SalesQuarterlyTrendResponseDTO {
	private final int year;
	private final int quarter;
	private final int totalAmount;
}
