package com.clover.salad.employee.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.query.dto.DepartmentEmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.DepartmentHierarchyDTO;
import com.clover.salad.employee.query.service.DepartmentQueryService;
import com.clover.salad.employee.query.service.EmployeeQueryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentQueryController {
	private final DepartmentQueryService departmentQueryService;
	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public DepartmentQueryController(DepartmentQueryService departmentQueryService,
		EmployeeQueryService employeeQueryService
	) {
		this.departmentQueryService = departmentQueryService;
		this.employeeQueryService = employeeQueryService;
	}

	/* 설명. 부서 계층 조회 */
	@GetMapping("/hierarchy")
	public ResponseEntity<List<DepartmentHierarchyDTO>> getDepartmentHierarchy() {
		return ResponseEntity.ok(departmentQueryService.getDepartmentHierarchy());
	}

	/* 설명. 해당 부서의 사원 전체 조회 */
	@GetMapping("/employee")
	public ResponseEntity<List<DepartmentEmployeeSearchResponseDTO>> getEmployeesByDepartment(
		@RequestParam("departmentId") int departmentId) {
		List<DepartmentEmployeeSearchResponseDTO> result =
			employeeQueryService.searchEmployeesByDepartmentWithSub(departmentId);
		return ResponseEntity.ok(result);
	}

}
