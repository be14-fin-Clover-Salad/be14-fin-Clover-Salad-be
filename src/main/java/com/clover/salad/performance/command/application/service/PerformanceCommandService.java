package com.clover.salad.performance.command.application.service;

public interface PerformanceCommandService {
	void refreshEmployeeContractPerformance(String employeeCode, int targetDate);
	
	void refreshEmployeeCustomerPerformance(String employeeCode, int targetDate);
	
	void refreshDepartmentPerformance(String deptName, int targetDate);
}
