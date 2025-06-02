package com.clover.salad.employee.command.application.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.domain.repository.EmployeeRepository;

@Service
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public EmployeeCommandServiceImpl(EmployeeRepository employeeRepository,
		ModelMapper modelMapper,
		BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.employeeRepository = employeeRepository;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


}