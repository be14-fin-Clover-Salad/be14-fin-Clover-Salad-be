package com.clover.salad.performance.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;

@Mapper
public interface PerformanceMapper {
	List<EmployeePerformanceDTO> selectEmployeePerformanceByEmployeeId(
		@Param("employeeId") int employeeId,
		@Param("startDate") Integer startDate,
		@Param("endDate") Integer endDate
	);
}
