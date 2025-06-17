package com.clover.salad.employee.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeSearchResponseDTO {
	private int id;
	private String code;
	private String name;
	private String phone;
	private String email;
	private String level;
	private String hireDate;
	private String workPlace;
	private int departmentId;
	private String departmentName;
}
