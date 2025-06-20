package com.clover.salad.employee.command.application.service;

import com.clover.salad.employee.command.application.dto.EmployeeUpdateDTO;
import com.clover.salad.employee.command.application.dto.RequestChangePasswordDTO;

public interface EmployeeCommandService {

	void sendResetPasswordLink(String code, String email);

	void confirmResetPassword(String token, String newPassword);

	void updateEmployee(int id, EmployeeUpdateDTO dto);

	void changePassword(int id, RequestChangePasswordDTO dto);

	void updateProfilePath(int id, String newPath);

	void updateProfile(int employeeId, int fileId);
}
