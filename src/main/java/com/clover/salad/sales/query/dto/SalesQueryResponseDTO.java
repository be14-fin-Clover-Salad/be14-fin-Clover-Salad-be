package com.clover.salad.sales.query.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesQueryResponseDTO {
	private Integer id;
	private LocalDate salesDate;
	private String department;
	private String employeeName;
	private Integer amount;
	private Integer contractId;
}
