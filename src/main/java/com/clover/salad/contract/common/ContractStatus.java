package com.clover.salad.contract.common;

import lombok.Getter;

@Getter
public enum ContractStatus {
	PENDING("결재전"),
	REJECTED("반려"),
	IN_PROGRESS("결재중"),
	IN_CONTRACT("계약중"),
	EXPIRED("계약만료"),
	TERMINATED("중도해지"),
	INVALID("계약무효");

	private final String label;

	ContractStatus(String label) {
		this.label = label;
	}

	public static final ContractStatus DEFAULT = PENDING;

	public static ContractStatus fromLabel(String label) {
		for (ContractStatus status : values()) {
			if (status.label.equals(label)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Unknown label: " + label);
	}
}
