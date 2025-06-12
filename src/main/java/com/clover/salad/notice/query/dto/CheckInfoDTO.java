package com.clover.salad.notice.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInfoDTO {
	private int employeeId;
	private String employeeName;
	private boolean isChecked;
}
