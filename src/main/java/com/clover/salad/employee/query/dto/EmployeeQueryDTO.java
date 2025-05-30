package com.clover.salad.employee.query.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeQueryDTO {
	private int id;
	private String code;
	private String name;
	private String phone;
	private String email;
	private String level;
	private LocalDate hireDate;
	private String workPlace;
	private int profileId;
	private DepartmentQueryDTO department;
}
