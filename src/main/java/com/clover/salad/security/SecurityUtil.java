package com.clover.salad.security;

import com.clover.salad.security.EmployeeDetails;
import com.clover.salad.security.token.TokenPrincipal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

	public static int getEmployeeId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new IllegalStateException("인증 정보가 존재하지 않습니다.");
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof EmployeeDetails) {
			return ((EmployeeDetails) principal).getId();
		} else if (principal instanceof TokenPrincipal) {
			return ((TokenPrincipal) principal).getEmployeeId();
		}

		throw new IllegalStateException("지원하지 않는 인증 주체 타입입니다: " + principal.getClass().getSimpleName());
	}

	public static boolean hasRole(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getAuthorities() == null) {
			return false;
		}
		return authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(auth -> auth.equals(role));
	}
}