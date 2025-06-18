package com.clover.salad.common.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
	private final HttpStatus status;

	public AuthException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public AuthException(String message) {
		this(message, HttpStatus.UNAUTHORIZED);
	}
}