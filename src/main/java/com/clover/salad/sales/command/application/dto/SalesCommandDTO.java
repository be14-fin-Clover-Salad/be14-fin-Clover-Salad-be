package com.clover.salad.sales.command.application.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesCommandDTO {
	private LocalDate salesDate;
	private String department;
	private String employeeName;
	private int amount;
	private int contractId;
}
