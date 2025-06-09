package com.clover.salad.common.exception;

public class EmployeeNotFoundException extends RuntimeException {
	public EmployeeNotFoundException() {
		super("Employee Code Not Found");
	}
}
