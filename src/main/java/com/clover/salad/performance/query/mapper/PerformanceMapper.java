package com.clover.salad.performance.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.performance.command.application.dto.DepartmentPerformanceDTO;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.application.dto.ProductPerformancePerMonthDTO;

@Mapper
public interface PerformanceMapper {
	List<EmployeePerformanceDTO> selectEmployeePerformanceByEmployeeId(
		@Param("employeeId") int employeeId,
		@Param("startDate") Integer startDate,
		@Param("endDate") Integer endDate
	);
	
	List<DepartmentPerformanceDTO> selectDepartmentPerformanceByDepartmentName(
		@Param("deptName") String deptName,
		@Param("startDate") Integer startDate,
		@Param("endDate") Integer endDate
	);
	
	List<ProductPerformancePerMonthDTO> selectProductPerformanceByProductCode(
		@Param("productCode") String productCode,
		@Param("startDate") String startDate,
		@Param("endDate") String endDate
	);
	
	List<EmployeePerformanceDTO> selectEmployeePerformanceByTargetDateAndDepartmentId(
		@Param("deptId") int deptId,
		@Param("targetDate") int targetDate
	);
}
