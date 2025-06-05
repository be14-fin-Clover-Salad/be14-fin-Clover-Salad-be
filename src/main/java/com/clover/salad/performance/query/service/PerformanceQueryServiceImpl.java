package com.clover.salad.performance.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.EmployeeNotFoundException;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.performance.command.application.dto.DepartmentPerformanceDTO;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.application.dto.SearchTermDTO;
import com.clover.salad.performance.query.mapper.PerformanceMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceQueryServiceImpl implements PerformanceQueryService {
	private final PerformanceMapper performanceMapper;
	private final EmployeeQueryService employeeQueryService;
	
	@Override
	public List<EmployeePerformanceDTO> searchEmployeePerformanceByEmployeeCode(
		String employeeCode,
		SearchTermDTO searchTerm
	) {
		return performanceMapper.selectEmployeePerformanceByEmployeeId(
			getEmployeeByCode(employeeCode).getId(),
			searchTerm.getStartDate(),
			searchTerm.getEndDate()
		);
	}
	
	@Override
	public List<DepartmentPerformanceDTO> searchDepartmentPerformanceByDepartmentName(
		String deptName,
		SearchTermDTO searchTerm
	) {
		return performanceMapper.selectDepartmentPerformanceByDepartmentName(
			deptName,
			searchTerm.getStartDate(),
			searchTerm.getEndDate()
		);
	}
	
	private EmployeeQueryDTO getEmployeeByCode(String employeeCode)
		throws EmployeeNotFoundException {
		SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
		searchEmployeeDTO.setCode(employeeCode);
		List<EmployeeQueryDTO> employeeList = employeeQueryService.searchEmployees(searchEmployeeDTO);
		if (employeeList.isEmpty()) throw new EmployeeNotFoundException();
		return employeeList.get(0);
	}
}
