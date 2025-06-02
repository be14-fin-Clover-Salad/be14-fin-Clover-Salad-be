package com.clover.salad.employee.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentQueryDTO {
	private int id;
	private String name;
	private Integer supDeptId;
}