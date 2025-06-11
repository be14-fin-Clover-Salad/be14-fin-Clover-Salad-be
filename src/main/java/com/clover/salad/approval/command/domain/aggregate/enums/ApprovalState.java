package com.clover.salad.approval.command.domain.aggregate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalState {

	REQUESTED("요청"),
	APPROVED("승인"),
	REJECTED("반려");

	private final String label;

	public static ApprovalState fromLabel(String label) {
		for (ApprovalState state : values()) {
			if (state.label.equals(label)) {
				return state;
			}
		}
		throw new IllegalArgumentException("Unknown label: " + label);
	}
}