package com.clover.salad.common.exception;

import java.util.Map;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidCurrentPasswordException.class)
	public ResponseEntity<String> handleInvalidPassword(InvalidCurrentPasswordException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(UnauthorizedEmployeeException.class)
	public ResponseEntity<String> handleUnauthorizedEmployee(UnauthorizedEmployeeException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<String> handleAuthException(AuthException e) {
		return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(InvalidSearchTermException.class)
	public ResponseEntity<String> handleInvalidSearchTerm(InvalidSearchTermException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
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

	// 기타 예상치 못한 런타임 예외
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntime(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
}
