package com.clover.salad.employee.command.application.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.application.dto.RequestRegisterDTO;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
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

	@Override
	public void registerUser(RequestRegisterDTO requestRegisterDTO) {
		requestRegisterDTO.setUserId(UUID.randomUUID().toString());
		EmployeeEntity registerUser = modelMapper.map(requestRegisterDTO, EmployeeEntity.class);
		registerUser.setEncPwd(bCryptPasswordEncoder.encode(requestRegisterDTO.getPassword()));
		employeeRepository.save(registerUser);
	}
}