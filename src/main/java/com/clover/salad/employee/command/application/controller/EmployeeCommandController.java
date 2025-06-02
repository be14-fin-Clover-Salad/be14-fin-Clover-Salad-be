package com.clover.salad.employee.command.application.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.command.application.service.EmployeeCommandService;

@RestController
@RequestMapping("/employee")
public class EmployeeCommandController {

	private final ModelMapper modelMapper;
	private final EmployeeCommandService employeeCommandService;

	@Autowired
	public EmployeeCommandController(ModelMapper modelMapper,
		EmployeeCommandService employeeCommandService) {
		this.modelMapper = modelMapper;
		this.employeeCommandService = employeeCommandService;
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
		String pureToken = token.replace("Bearer ", "");
		employeeCommandService.logout(pureToken);
		return ResponseEntity.ok("로그아웃 성공 (토큰 블랙리스트 등록 완료)");
	}
}