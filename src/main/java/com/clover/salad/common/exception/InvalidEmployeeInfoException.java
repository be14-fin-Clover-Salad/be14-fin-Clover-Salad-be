package com.clover.salad.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmployeeInfoException extends RuntimeException {
	public InvalidEmployeeInfoException() {
		super("해당 사번 또는 이메일이 존재하지 않습니다.");
	}
}