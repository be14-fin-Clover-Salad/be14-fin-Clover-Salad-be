package com.clover.salad.employee.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
	private String accessToken;
	private LoginHeaderInfoDTO loginHeaderInfo;
}