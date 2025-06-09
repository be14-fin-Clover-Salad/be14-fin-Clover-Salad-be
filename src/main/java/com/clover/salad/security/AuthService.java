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

	public UserDetails loadUserDetailsById(String id) {
		return employeeQueryService.loadUserByUsername(id); // id는 String으로 들어가지만 내부에서 int로 파싱됨
	}
}