package com.clover.salad.common.exception;

public class InvalidSearchTermException extends RuntimeException {
	public InvalidSearchTermException() {
		super("목표의 조건을 확인하고 설정해주세요!");
	}
}
