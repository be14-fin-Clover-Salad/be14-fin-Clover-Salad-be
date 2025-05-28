package com.clover.salad.employee.command.application.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.application.dto.RegisterRequestDTO;
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
	public void registerUser(RegisterRequestDTO registerRequestDTO) {

		registerRequestDTO.setUserId(UUID.randomUUID().toString());

		/* 설명. Entity로 modelMapper로 매핑 후 엔티티에 있는 encryptedPwd에 암호화된 값을 추가한다. */
		EmployeeEntity registerUser = modelMapper.map(registerRequestDTO, EmployeeEntity.class);
		registerUser.setEncPwd(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()));

		employeeRepository.save(registerUser);
	}
}
