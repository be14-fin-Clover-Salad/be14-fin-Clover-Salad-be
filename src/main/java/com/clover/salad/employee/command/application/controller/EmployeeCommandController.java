package com.clover.salad.employee.command.application.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.command.application.dto.RequestRegisterDTO;
import com.clover.salad.employee.command.application.service.EmployeeCommandService;
import com.clover.salad.employee.command.domain.aggregate.vo.RequestRegisterEmployeeVO;
import com.clover.salad.employee.command.domain.aggregate.vo.ResponseRegisterEmployeeVO;

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

	@PostMapping
	public ResponseEntity<ResponseRegisterEmployeeVO> registerUser(@RequestBody RequestRegisterEmployeeVO newUser) {
		RequestRegisterDTO dto = modelMapper.map(newUser, RequestRegisterDTO.class);
		employeeCommandService.registerUser(dto);
		ResponseRegisterEmployeeVO response = modelMapper.map(dto, ResponseRegisterEmployeeVO.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}