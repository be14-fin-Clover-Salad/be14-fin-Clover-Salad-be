package com.clover.salad.employee.command.application.service;

import com.clover.salad.employee.command.application.dto.EmployeeUpdateDTO;

public interface EmployeeCommandService {
	void logout(String pureToken);

	void sendResetPasswordLink(String code, String email);

	void confirmResetPassword(String token, String newPassword);

	void updateEmployee(String code, EmployeeUpdateDTO dto);
}
