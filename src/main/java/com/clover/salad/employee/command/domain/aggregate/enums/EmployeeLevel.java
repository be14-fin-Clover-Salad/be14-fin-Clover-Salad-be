package com.clover.salad.employee.command.domain.aggregate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeLevel {
	L1("사원"),
	L2("주임"),
	L3("대리"),
	L4("과장"),
	L5("팀장"),
	ADMIN("관리자");

	private final String label;

	public static EmployeeLevel fromLabel(String label) {
		for (EmployeeLevel level : values()) {
			if (level.label.equals(label)) {
				return level;
			}
		}
		throw new IllegalArgumentException("알 수 없는 직급: " + label);
	}
}