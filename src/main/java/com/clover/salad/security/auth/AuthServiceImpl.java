package com.clover.salad.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.service.EmployeeQueryService;

@Service
public class AuthServiceImpl implements AuthService {

	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public AuthServiceImpl(EmployeeQueryService employeeQueryService) {
		this.employeeQueryService = employeeQueryService;
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