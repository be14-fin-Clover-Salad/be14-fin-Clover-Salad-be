package com.clover.salad.employee.command.application.service;

import com.clover.salad.employee.command.application.dto.RegisterRequestDTO;

public interface EmployeeCommandService {
	void registerUser(RegisterRequestDTO registerRequestDTO);
}
