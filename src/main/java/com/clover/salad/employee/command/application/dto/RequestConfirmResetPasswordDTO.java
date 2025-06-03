package com.clover.salad.employee.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestConfirmResetPasswordDTO {
	private String token;
	private String newPassword;
}
