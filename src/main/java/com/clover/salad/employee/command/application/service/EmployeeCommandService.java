package com.clover.salad.employee.command.application.service;

public interface EmployeeCommandService {
	void logout(String pureToken);

	void sendResetPasswordLink(String code, String email);

	void confirmResetPassword(String token, String newPassword);
}
