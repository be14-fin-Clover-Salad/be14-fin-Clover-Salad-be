package com.clover.salad.contract.command.dto;

import com.clover.salad.contract.command.entity.CustomerEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
	private String name;
	private String birthdate;
	private String gender;
	private String phone;
	private String address;
	private String email;

	public CustomerEntity toEntity() {
		return CustomerEntity.builder()
			.name(name)
			.birthdate(birthdate)
			.gender(gender)
			.phone(phone)
			.address(address)
			.email(email)
			.build();
	}
}