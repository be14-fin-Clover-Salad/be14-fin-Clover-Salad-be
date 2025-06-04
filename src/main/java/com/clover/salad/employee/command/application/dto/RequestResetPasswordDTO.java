package com.clover.salad.employee.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestResetPasswordDTO {
	private String code;
	private String email;
}
