package com.clover.salad.contract.command.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.common.ContractStatus;
import com.clover.salad.contract.common.RelationshipType;
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


	public ContractEntity toEntityWithDefaults(
		Customer customer,
		String contractCode,
		DocumentOrigin documentOrigin,
		EmployeeEntity employee // 사원 엔티티 주입받음
	) {
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
			.relationship(RelationshipType.from(relationship))
			.paymentEmail(paymentEmail)
			.status(ContractStatus.DEFAULT)
			.isDeleted(false)
			.etc(null)
			.documentOrigin(documentOrigin)
			.customer(customer)
			.employee(employee) // 로그인한 사원 주입
			.build();
	}


}