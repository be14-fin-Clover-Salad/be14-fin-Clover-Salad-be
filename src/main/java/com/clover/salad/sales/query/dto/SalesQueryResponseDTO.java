package com.clover.salad.sales.query.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesQueryResponseDTO {
	private int id;
	private LocalDate salesDate;
	private String department;
	private String employeeName;
	private int amount;
	private String contractCode;
}
