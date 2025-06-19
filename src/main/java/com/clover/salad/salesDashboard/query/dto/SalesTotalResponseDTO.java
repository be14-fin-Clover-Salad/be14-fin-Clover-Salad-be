package com.clover.salad.salesDashboard.query.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SalesTotalResponseDTO {
	private final String period;
	private final LocalDate StartDate;
	private final LocalDate EndDate;
	private final int totalAmount;
}
