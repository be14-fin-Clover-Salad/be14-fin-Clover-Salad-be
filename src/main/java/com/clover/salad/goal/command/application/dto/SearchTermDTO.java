package com.clover.salad.goal.command.application.dto;

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
	private Integer targetId;
	private Integer startDate;
	private Integer endDate;
	private Integer minRentalProductCount;
	private Integer maxRentalProductCount;
	private Integer minRentalRetentionRate;
	private Integer maxRentalRetentionRate;
	private Integer minNewCustomerCount;
	private Integer maxNewCustomerCount;
	private Integer minTotalRentalAmount;
	private Integer maxTotalRentalAmount;
	private Integer minCustomerFeedbackScore;
	private Integer maxCustomerFeedbackScore;
}
