package com.clover.salad.salesDashboard.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SalesTeamAmountResponseDTO {
	private String teamName;
	private int teamAmount;
}
