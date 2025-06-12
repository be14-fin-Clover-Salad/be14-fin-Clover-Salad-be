package com.clover.salad.performance.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EmployeePerformanceDTO {
	private Integer id;
	
	private Integer rentalProductCount = 0;
	
	private Integer rentalRetentionCount = 0;
	private Integer totalRentalCount = 0;
	
	private Integer newCustomerCount = 0;
	
	private Long totalRentalAmount = 0L;
	
	/* 설명. 1.0 -> 10으로 내부적으로 처리하고 외부로 보일 때는 사이에 .찍어서 표시하기 */
	private Integer customerFeedbackScore = 0;
	private Integer customerFeedbackCount = 0;
	
	private int targetDate;
	private int employeeId;
}
