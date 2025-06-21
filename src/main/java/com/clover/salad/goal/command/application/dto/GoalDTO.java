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
public class GoalDTO {
	private Integer id;
	
	private Integer rentalProductCount;
	
	private Integer rentalRetentionCount;
	private Integer totalRentalCount;
	
	private Integer newCustomerCount;
	
	private Long totalRentalAmount;
	
	/* 설명. 1.0 -> 10으로 내부적으로 처리하고 외부로 보일 때는 사이에 .찍어서 표시하기 */
	private Integer customerFeedbackScore;
	private Integer customerFeedbackCount;
	
	private int targetDate;
	private int employeeId;
	private Integer employeeCode;
}
