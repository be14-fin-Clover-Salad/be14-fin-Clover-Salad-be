package com.clover.salad.contract.common;

public enum ContractStatus {
	결제전, 반려, 결재중, 계약만료, 중도해지;

	public static final ContractStatus DEFAULT = 결제전;
}
