package com.clover.salad.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidCurrentPasswordException.class)
	public ResponseEntity<String> handleInvalidPassword(InvalidCurrentPasswordException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("InvalidCurrentPasswordException - User: {}, [{} {}] {}", userId, request.getMethod(), request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntime(RuntimeException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.error("RuntimeException - User: {}, [{} {}] {}", userId, request.getMethod(), request.getRequestURI(), e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("EmployeeNotFoundException - User: {}, [{} {}] {}", userId, request.getMethod(), request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(InvalidSearchTermException.class)
	public ResponseEntity<String> handleInvalidSearchTerm(InvalidSearchTermException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("InvalidSearchTermException - User: {}, [{} {}] {}", userId, request.getMethod(), request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(UnauthorizedEmployeeException.class)
	public ResponseEntity<String> handleUnauthorizedEmployee(UnauthorizedEmployeeException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("UnauthorizedEmployeeException - User: {}, [{} {}] {}", userId, request.getMethod(), request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<String> handleAuthException(AuthException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("AuthException - User: {}, [{} {}] {}", userId, request.getMethod(), request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	}

	// 공통 사용자 ID 추출 함수
	private String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated() &&
			!"anonymousUser".equals(authentication.getPrincipal())) {
			return authentication.getName(); // 또는 CustomUserDetails에서 getEmployeeId()
		}
		return "Anonymous";
	}
}
