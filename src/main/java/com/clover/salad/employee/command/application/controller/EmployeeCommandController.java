package com.clover.salad.employee.command.application.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.command.application.dto.RegisterRequestDTO;
import com.clover.salad.employee.command.application.service.EmployeeCommandService;
import com.clover.salad.employee.command.domain.aggregate.vo.RequestRegisterEmployeeVO;
import com.clover.salad.employee.command.domain.aggregate.vo.ResponseRegisterEmployeeVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmployeeCommandController {
	private final Environment env;
	private final ModelMapper modelMapper;
	private final EmployeeCommandService employeeCommandService;

	@Autowired
	public EmployeeCommandController(Environment env,
									ModelMapper modelMapper,
									EmployeeCommandService employeeCommandService
		) {
		this.env = env;
		this.modelMapper = modelMapper;
		this.employeeCommandService = employeeCommandService;
	}

	@PostMapping("/employee")
	public ResponseEntity<ResponseRegisterEmployeeVO> registerUser(@RequestBody RequestRegisterEmployeeVO newUser) {

		RegisterRequestDTO registerRequestDTO = modelMapper.map(newUser, RegisterRequestDTO.class);
		employeeCommandService.registerUser(registerRequestDTO);

		ResponseRegisterEmployeeVO successRegisterUser =
			modelMapper.map(registerRequestDTO, ResponseRegisterEmployeeVO.class);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(successRegisterUser);
	}

}
