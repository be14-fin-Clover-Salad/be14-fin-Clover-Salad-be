package com.clover.salad.employee.command.application.service;

import com.clover.salad.employee.command.application.dto.RequestRegisterDTO;

public interface EmployeeCommandService {
	void registerUser(RequestRegisterDTO requestRegisterDTO);
}
