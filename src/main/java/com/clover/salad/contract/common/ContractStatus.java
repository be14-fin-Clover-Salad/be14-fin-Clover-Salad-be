package com.clover.salad.contract.common;

public enum ContractStatus {
	결재전, 반려, 결재중, 계약만료, 중도해지, 계약무효;

	public static final ContractStatus DEFAULT = 결재전;
}
