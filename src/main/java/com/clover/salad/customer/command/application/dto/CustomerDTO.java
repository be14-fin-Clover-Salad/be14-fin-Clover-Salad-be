package com.clover.salad.customer.command.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDTO {
	private int id;
	private String name;
	private String birthdate;
	private String phone;
	private String email;
	private String registerAt;
	@JsonProperty("isDeleted")
	private boolean isDeleted;
	private String type;
	private String etc;
}
