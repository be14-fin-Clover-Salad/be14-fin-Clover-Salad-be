package com.clover.salad.performance.query.service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.EmployeeNotFoundException;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchRequestDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.performance.command.application.dto.DepartmentPerformanceDTO;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.application.dto.ProductPerformancePerMonthDTO;
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
	
	@Override
	public List<ProductPerformancePerMonthDTO> searchProductPerformanceByProductCode(String productCode,
		SearchTermDTO searchTerm) {
		return performanceMapper.selectProductPerformanceByProductCode(
			productCode,
			getDayOfMonth(searchTerm.getStartDate(), true),
			getDayOfMonth(searchTerm.getEndDate(), false)
		);
	}
	
	@Override
	public List<EmployeePerformanceDTO> searchEmployeePerformanceByTargetDateAndDepartmentId(int deptId, int targetDate) {
		return performanceMapper.selectEmployeePerformanceByTargetDateAndDepartmentId(deptId, targetDate);
	}
	
	public static String getDayOfMonth(int yyyyMM, boolean isFirst) {
		
		int year = yyyyMM / 100;
		int month = yyyyMM % 100;
		
		YearMonth yearMonth = YearMonth.of(year, month);
		
		int day;
		
		if (isFirst) {
			day = 1;
		} else {
			day = yearMonth.lengthOfMonth();
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return yearMonth.atDay(day).format(formatter);
	}

	// DTO 변경
	private EmployeeSearchResponseDTO getEmployeeByCode(String employeeCode)
		throws EmployeeNotFoundException {
		EmployeeSearchRequestDTO searchDTO = new EmployeeSearchRequestDTO();
		searchDTO.setCode(employeeCode);
		List<EmployeeSearchResponseDTO> employeeList = employeeQueryService.searchEmployees(searchDTO);
		if (employeeList.isEmpty()) throw new EmployeeNotFoundException();
		return employeeList.get(0);
	}
	// private EmployeeQueryDTO getEmployeeByCode(String employeeCode)
	// 	throws EmployeeNotFoundException {
	// 	SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();
	// 	searchEmployeeDTO.setCode(employeeCode);
	// 	List<EmployeeQueryDTO> employeeList = employeeQueryService.searchEmployees(searchEmployeeDTO);
	// 	if (employeeList.isEmpty()) throw new EmployeeNotFoundException();
	// 	return employeeList.get(0);
	// }
}
