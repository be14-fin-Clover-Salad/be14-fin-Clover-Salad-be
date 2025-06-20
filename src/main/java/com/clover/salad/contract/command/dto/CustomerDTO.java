package com.clover.salad.contract.command.dto;

import java.time.LocalDate;

import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerDTO {
	private String name;
	private String birthdate;
	private String address;
	private String phone;
	private String email;
	private CustomerType customerType;

	public Customer toEntityWithDefaults() {
		return Customer.builder().name(name).birthdate(birthdate).phone(phone).address(address)
				.email(email).type(customerType).registerAt(LocalDate.now()).isDeleted(false)
				.etc(null).build();
	}
}
