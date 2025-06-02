package com.clover.salad.employee.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmployeeQueryController {
	private final Environment env;
	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public EmployeeQueryController(Environment env, EmployeeQueryService employeeQueryService) {
		this.env = env;
		this.employeeQueryService = employeeQueryService;
	}

	@GetMapping("/health")
	public String status() {
		return "서버가 동작 중입니다. 포트: " + env.getProperty("local.server.port");
	}

	@PostMapping("/employee/search")
	public ResponseEntity<List<EmployeeQueryDTO>> searchEmployees(@RequestBody SearchEmployeeDTO searchEmployeeDTO) {
		log.info("사원 조건 검색 요청: {}", searchEmployeeDTO);

		List<EmployeeQueryDTO> employees = employeeQueryService.searchEmployees(searchEmployeeDTO);

		return ResponseEntity.ok(employees);
	}

}
