package com.clover.salad.employee.command.application.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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


}