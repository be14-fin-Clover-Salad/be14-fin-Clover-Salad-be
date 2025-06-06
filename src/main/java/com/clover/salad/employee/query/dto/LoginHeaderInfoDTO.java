package com.clover.salad.employee.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginHeaderInfoDTO {
	private String name;
	private String levelLabel;
	private String profilePath;
	private String departmentName;
}
