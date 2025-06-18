package com.clover.salad.employee.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepartmentQueryDTO {
	private int id;
	private String name;
	private Integer supDeptId;
}
