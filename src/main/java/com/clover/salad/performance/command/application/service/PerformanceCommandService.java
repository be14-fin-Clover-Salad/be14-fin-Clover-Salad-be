package com.clover.salad.performance.command.application.service;

public interface PerformanceCommandService {
	void refreshEmployeePerformance(String employeeCode, int targetDate);
	
	void refreshDepartmentPerformance(String deptName, int targetDate);
}
