package com.clover.salad.common.exception;

public class InvalidEmailFormatException extends RuntimeException {
	public InvalidEmailFormatException() {
		super("유효한 이메일 형식이 아닙니다.");
	}
}