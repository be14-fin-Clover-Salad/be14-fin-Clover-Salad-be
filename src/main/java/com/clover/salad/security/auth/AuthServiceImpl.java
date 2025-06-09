package com.clover.salad.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.service.EmployeeQueryService;

@Service
public class AuthServiceImpl implements AuthService {

	private final EmployeeQueryService employeeQueryService;
	private final EmployeeRepository employeeRepository;

	@Autowired
	public AuthServiceImpl(EmployeeQueryService employeeQueryService,
		EmployeeRepository employeeRepository) {
		this.employeeQueryService = employeeQueryService;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public UserDetails loadUserById(int id) {
		return employeeQueryService.loadUserById(id);
	}

	@Override
	public String findCodeByEmployeeId(int id) {
		return employeeQueryService.findCodeById(id);
	}
}