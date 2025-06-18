package com.clover.salad.employee.query.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchEmployeeDTO {
	private String code;
	private String name;
	private String phone;
	private String email;
	private String level;

	// 날짜 범위 검색
	private LocalDate hireDateFrom;
	private LocalDate hireDateTo;

	private String workPlace;

	private String departmentName;
}
