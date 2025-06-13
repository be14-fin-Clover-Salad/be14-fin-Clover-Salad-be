package com.clover.salad.employee.query.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDetailDTO {
	private String name;
	private String level;
	private String phone;
	private String email;
	private String departmentName;
	private String code;
	private LocalDate hireDate;
	private String workPlace;
}
