package com.clover.salad.salesDashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SalesYearlyTrendResponseDTO {
	private final int year;
	private final int totalAmount;
}
