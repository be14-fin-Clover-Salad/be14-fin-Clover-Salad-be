package com.clover.salad.contract.command.dto;

import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.entity.CustomerEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
	private String contractNumber;
	private String productName;
	private String monthlyFee;
	private String totalFee;
	private String rentalPeriod;
	private String paymentMethod;

	public ContractEntity toEntity(CustomerEntity customer) {
		return ContractEntity.builder()
			.contractNumber(contractNumber)
			.productName(productName)
			.monthlyFee(monthlyFee)
			.totalFee(totalFee)
			.rentalPeriod(rentalPeriod)
			.paymentMethod(paymentMethod)
			.customer(customer)
			.build();
	}
}