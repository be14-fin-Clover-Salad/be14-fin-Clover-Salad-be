package com.clover.salad.security;

import com.clover.salad.security.EmployeeDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

	public static int getEmployeeId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof EmployeeDetails)) {
			throw new IllegalStateException("인증 정보가 존재하지 않거나 타입이 맞지 않습니다.");
		}
		return ((EmployeeDetails) authentication.getPrincipal()).getId();
	}

	public static EmployeeDetails getEmployee() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof EmployeeDetails)) {
			throw new IllegalStateException("인증 정보가 존재하지 않거나 타입이 맞지 않습니다.");
		}
		return (EmployeeDetails) authentication.getPrincipal();
	}
}