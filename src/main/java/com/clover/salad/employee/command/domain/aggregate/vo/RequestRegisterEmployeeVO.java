package com.clover.salad.employee.command.domain.aggregate.vo;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class RequestRegisterEmployeeVO {
	private int code;
	private String password;
	private String name;
	private String phone;
	private String email;
	private String level;
	private LocalDate hireDate;
	private LocalDate resignDate;
	private boolean isAdmin = false;
	private boolean isDeleted = false;
	private String workPlace;

	private int departmentId;
	private int profile;

}