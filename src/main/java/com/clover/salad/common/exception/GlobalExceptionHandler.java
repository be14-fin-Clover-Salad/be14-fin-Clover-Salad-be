package com.clover.salad.common.exception;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidCurrentPasswordException.class)
	public ResponseEntity<String> handleInvalidPassword(InvalidCurrentPasswordException e,
			HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("InvalidCurrentPasswordException - User: {}, [{} {}] {}", userId,
				request.getMethod(), request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntime(RuntimeException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.error("RuntimeException - User: {}, [{} {}] {}", userId, request.getMethod(),
				request.getRequestURI(), e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e,
			HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("EmployeeNotFoundException - User: {}, [{} {}] {}", userId, request.getMethod(),
				request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(InvalidSearchTermException.class)
	public ResponseEntity<String> handleInvalidSearchTerm(InvalidSearchTermException e,
			HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("InvalidSearchTermException - User: {}, [{} {}] {}", userId, request.getMethod(),
				request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(UnauthorizedEmployeeException.class)
	public ResponseEntity<String> handleUnauthorizedEmployee(UnauthorizedEmployeeException e,
			HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("UnauthorizedEmployeeException - User: {}, [{} {}] {}", userId,
				request.getMethod(), request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<String> handleAuthException(AuthException e, HttpServletRequest request) {
		String userId = getCurrentUserId();
		log.warn("AuthException - User: {}, [{} {}] {}", userId, request.getMethod(),
				request.getRequestURI(), e.getMessage());
		return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	}

	// 공통 사용자 ID 추출 함수
	private String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {
			return authentication.getName(); // 또는 CustomUserDetails에서 getEmployeeId()
		}
		return "Anonymous";
	}

	// 고객 관련 - 커스텀 예외
	@ExceptionHandler(CustomersException.InvalidCustomerDataException.class)
	public ResponseEntity<?> handleInvalidCustomerData(
			CustomersException.InvalidCustomerDataException ex) {
		return ResponseEntity.badRequest()
				.body(Map.of("status", 400, "error", "잘못된 고객 정보", "message", ex.getMessage()));
	}

	// @Valid 유효성 검증 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getAllErrors().stream().findFirst()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("잘못된 요청입니다.");

		return ResponseEntity.badRequest()
				.body(Map.of("status", 400, "error", "잘못된 고객 정보", "message", errorMessage));
	}

	// JSON 파싱 실패 (e.g. enum 잘못 입력)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		Throwable cause = ex.getCause();

		if (cause instanceof InvalidFormatException) {
			Throwable rootCause = cause.getCause();
			String rawMessage = (rootCause != null) ? rootCause.getMessage() : cause.getMessage();

			String cleanedMessage = rawMessage.split("\n")[0];

			return ResponseEntity.badRequest()
					.body(Map.of("status", 400, "error", "입력 오류", "message", cleanedMessage));
		}

		return ResponseEntity.badRequest()
				.body(Map.of("status", 400, "error", "입력 오류", "message", "잘못된 요청 형식입니다."));
	}

}
