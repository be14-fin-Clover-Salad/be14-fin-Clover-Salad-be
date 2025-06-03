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
public class DefaultGoalDTO {
	private int id;
	private String level;
	private Integer rentalProductCount;
	private Integer rentalRetentionRate;
	private Integer newCustomerCount;
	private Long totalRentalAmount;
	private Integer customerFeedbackScore;
	private Integer targetYear;
}
