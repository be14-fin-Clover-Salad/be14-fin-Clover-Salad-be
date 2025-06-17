package com.clover.salad.notice.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInfoDTO {
	private int employeeId;
	private String employeeName;
	private String employeeLevel;
	@JsonProperty("isChecked")
	private boolean isChecked;
}
