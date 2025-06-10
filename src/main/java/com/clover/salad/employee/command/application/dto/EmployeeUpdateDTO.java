package com.clover.salad.employee.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class EmployeeUpdateDTO {
	private String name;
	private String email;
	private String phone;
}