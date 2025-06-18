package com.clover.salad.performance.query.service;

import java.util.List;

import com.clover.salad.performance.command.application.dto.DepartmentPerformanceDTO;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.application.dto.ProductPerformancePerMonthDTO;
import com.clover.salad.performance.command.application.dto.SearchTermDTO;

public interface PerformanceQueryService {
	List<EmployeePerformanceDTO> searchEmployeePerformanceByEmployeeCode(
		String employeeCode,
		SearchTermDTO searchTerm
	);
	
	List<DepartmentPerformanceDTO> searchDepartmentPerformanceByDepartmentName(
		String deptName,
		SearchTermDTO searchTerm
	);
	
	List<ProductPerformancePerMonthDTO> searchProductPerformanceByProductCode(
		String productCode,
		SearchTermDTO searchTerm
	);
	
	List<EmployeePerformanceDTO> searchEmployeePerformanceByTargetDateAndDepartmentId(
		int deptId,
		int targetDate
	);
}
