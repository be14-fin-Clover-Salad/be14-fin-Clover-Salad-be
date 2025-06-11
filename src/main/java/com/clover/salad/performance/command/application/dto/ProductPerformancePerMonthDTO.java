package com.clover.salad.performance.command.application.dto;

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
// 월별 상품 실적
public class ProductPerformancePerMonthDTO {
	// ID용 상품 id
	private int productId;
	// 상품 코드
	private String productCode;
	// 상품 판매량
	private int sales;
}
