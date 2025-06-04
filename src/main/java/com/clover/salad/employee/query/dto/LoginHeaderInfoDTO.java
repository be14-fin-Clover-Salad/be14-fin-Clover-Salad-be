package com.clover.salad.employee.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginHeaderInfoDTO {
	private String name;
	private String departmentName;
	private String profileImagePath; // S3 경로
}
