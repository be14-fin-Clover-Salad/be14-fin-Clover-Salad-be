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
public class DepartmentPerformanceDTO {
	private Integer id;
	
	@Builder.Default
	private Integer rentalProductCount = 0;
	
	@Builder.Default
	private Integer rentalRetentionCount = 0;
	@Builder.Default
	private Integer totalRentalCount = 0;
	
	@Builder.Default
	private Integer newCustomerCount = 0;
	
	@Builder.Default
	private Long totalRentalAmount = 0L;
	
	/* 설명. 1.0 -> 10으로 내부적으로 처리하고 외부로 보일 때는 사이에 .찍어서 표시하기 */
	@Builder.Default
	private Integer customerFeedbackScore = 0;
	@Builder.Default
	private Integer customerFeedbackCount = 0;
	
	private int targetDate;
	private int departmentId;
}
