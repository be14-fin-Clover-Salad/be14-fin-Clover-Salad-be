package com.clover.salad.contract.exception;

public class ContractUploadFailedException extends RuntimeException {
	public ContractUploadFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
