package com.clover.salad.contract.command.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
		DocumentOrigin documentOrigin) {
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
			.build();
	}
}
