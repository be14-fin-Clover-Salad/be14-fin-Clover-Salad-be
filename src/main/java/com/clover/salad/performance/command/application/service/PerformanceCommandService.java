package com.clover.salad.performance.command.application.service;

public interface PerformanceCommandService {
	void refreshEmployeeContractPerformance(String employeeCode);
	
	void refreshEmployeeCustomerPerformance(String employeeCode);
	
	void refreshDepartmentPerformance(String deptName);
}
