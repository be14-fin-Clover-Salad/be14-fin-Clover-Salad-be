package com.clover.salad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.service.EmployeeQueryService;

@Service
public class AuthService {

	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public AuthService(EmployeeQueryService employeeQueryService) {
		this.employeeQueryService = employeeQueryService;
	}

	public UserDetails loadUserDetails(String code) {
		return employeeQueryService.loadUserByUsername(code);
	}
}
