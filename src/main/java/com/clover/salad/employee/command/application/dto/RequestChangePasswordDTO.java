package com.clover.salad.employee.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChangePasswordDTO {
	private String currentPassword;
	private String newPassword;
}
