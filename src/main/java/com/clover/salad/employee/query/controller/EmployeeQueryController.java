package com.clover.salad.employee.query.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.command.domain.aggregate.vo.ResponseFindEmployeeVO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmployeeQueryController {

	private final Environment env;
	private final EmployeeQueryService employeeQueryService;
	private final ModelMapper modelMapper;

	@Autowired
	public EmployeeQueryController(Environment env,
									EmployeeQueryService employeeQueryService,
									ModelMapper modelMapper) {
		this.env = env;
		this.employeeQueryService = employeeQueryService;
		this.modelMapper = modelMapper;

	}

	@GetMapping("/health")
	public String status() {
		return "서버가 동작 중입니다. 포트: " + env.getProperty("local.server.port");
	}

	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<ResponseFindEmployeeVO> getUsers(@PathVariable String employeeId) {
		EmployeeQueryDTO userDTO = employeeQueryService.getEmployeeById(employeeId);

		ResponseFindEmployeeVO findUserVO = modelMapper.map(userDTO, ResponseFindEmployeeVO.class);

		return ResponseEntity.status(HttpStatus.OK)
			.body(findUserVO);
	}

}
