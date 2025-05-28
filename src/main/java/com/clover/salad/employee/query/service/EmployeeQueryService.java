package com.clover.salad.employee.query.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.EmployeeQueryDTO;

@Service
public interface EmployeeQueryService extends UserDetailsService {
	EmployeeQueryDTO getEmployeeById(String employeeId);

	UserDetails loadUserByUsername(String code);
}
