package com.clover.salad.contract.command.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContractDTO {
	private LocalDate startDate;
	private LocalDate endDate;
	private int amount;
	private String bankName;
	private String bankAccount;
	private int paymentDay;
	private String depositOwner;
	private String relationship;
	private String paymentEmail;

	// 임시 사원 ID 하드코딩
	/*
	 * 로그인한 사원 정보를 가지고 할 예정임
	 * */

	public ContractEntity toEntityWithDefaults(
		Customer customer,
		String contractCode,
		DocumentOrigin documentOrigin
	) {
		// 임시 사원 ID 하드코딩
		EmployeeEntity dummyEmployee = EmployeeEntity.builder()
			.id(1) // 실제 DB에 존재하는 사원 ID
			.build();

		return ContractEntity.builder()
			.code(contractCode)
			.createdAt(LocalDateTime.now())
			.startDate(startDate)
			.endDate(endDate)
			.amount(amount)
			.bankName(bankName)
			.bankAccount(bankAccount)
			.paymentDay(paymentDay)
			.depositOwner(depositOwner)
			.relationship(relationship)
			.paymentEmail(paymentEmail)
			.status("결재 전")
			.isDeleted(false)
			.etc(null)
			.documentOrigin(documentOrigin)
			.customer(customer)
			.employee(dummyEmployee)
			.build();
	}

}