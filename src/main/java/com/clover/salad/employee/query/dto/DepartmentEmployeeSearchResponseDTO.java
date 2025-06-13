package com.clover.salad.employee.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentEmployeeSearchResponseDTO {
	private int id;
	private String name;
	private String level;
	private String phone;
	private String email;
	private String departmentName;
	private String path;
}
