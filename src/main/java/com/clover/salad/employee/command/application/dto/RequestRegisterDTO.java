package com.clover.salad.employee.command.application.dto;

import java.time.LocalDate;

import com.clover.salad.employee.command.domain.aggregate.enums.EmployeeLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestRegisterDTO {
	private int code;
	private String password;
	private String name;
	private String phone;
	private String email;
	private EmployeeLevel level;
	private LocalDate hireDate;
	private LocalDate resignDate;
	private boolean isAdmin;
	private boolean isDeleted;
	private String workPlace;
	private int departmentId;
	private int profile;
	private String userId;
}
