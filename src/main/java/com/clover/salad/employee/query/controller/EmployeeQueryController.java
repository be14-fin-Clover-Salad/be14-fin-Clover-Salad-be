package com.clover.salad.employee.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.security.SecurityUtil;

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

	@GetMapping("/employee/header")
	public ResponseEntity<LoginHeaderInfoDTO> getLoginHeaderInfo() {
		int employeeId = SecurityUtil.getEmployeeId();
		log.info("로그인 사용자 ID: {}", employeeId);
		LoginHeaderInfoDTO dto = employeeQueryService.getLoginHeaderInfoById(employeeId);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/employee/mypage")
	public ResponseEntity<EmployeeMypageQueryDTO> getMyPageInfo() {
		int employeeId = SecurityUtil.getEmployeeId();
		log.info("마이페이지 조회 사용자 ID: {}", employeeId);
		EmployeeMypageQueryDTO dto = employeeQueryService.getMyPageInfoById(employeeId);
		return ResponseEntity.ok(dto);
	}
}