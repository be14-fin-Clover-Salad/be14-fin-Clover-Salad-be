package com.clover.salad.salesDashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class SalesTotalRequestDTO {
	private final String period;	// 월별 / 분기별 / 연도별
}
