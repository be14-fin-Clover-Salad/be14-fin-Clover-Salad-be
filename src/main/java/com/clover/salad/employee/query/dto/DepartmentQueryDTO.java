package com.clover.salad.employee.query.dto;

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
public class DepartmentQueryDTO {
	private int id;
	private String name;
	private boolean isDeleted;
	private int supDeptId;
}