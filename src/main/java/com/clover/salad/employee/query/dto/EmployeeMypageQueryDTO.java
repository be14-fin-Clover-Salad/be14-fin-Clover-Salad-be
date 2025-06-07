package com.clover.salad.employee.query.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeMypageQueryDTO {
	private String code;
	private String name;
	private String phone;
	private String email;
	private String level;
	private LocalDate hireDate;
	private String workPlace;
	private String departmentName;
	private String profilePath;
}
