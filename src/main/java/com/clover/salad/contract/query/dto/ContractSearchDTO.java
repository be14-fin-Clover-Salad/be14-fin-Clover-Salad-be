package com.clover.salad.contract.query.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
public class ContractSearchDTO {
	private Integer employeeId;
	private String code;
	private LocalDateTime createdAtStart;
	private LocalDateTime createdAtEnd;
	private LocalDate startDateStart;
	private LocalDate startDateEnd;
	private LocalDate endDateStart;
	private LocalDate endDateEnd;
	private String status;
	private String bankName;
	private Integer paymentDayStart;
	private Integer paymentDayEnd;
	private String depositOwner;
	private String relationship;
	private String employeeName;
	private String customerName;
	private String productName;
}
