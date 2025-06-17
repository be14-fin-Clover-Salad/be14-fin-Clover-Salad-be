package com.clover.salad.customer.command.domain.aggregate.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerType {
	고객("고객"), 리드("리드");

	private final String label;

	CustomerType(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

	@JsonCreator
	public static CustomerType from(String input) {
		for (CustomerType type : values()) {
			if (type.label.equals(input)) {
				return type;
			}
		}
		throw new IllegalArgumentException("고객 유형은 '고객' 또는 '리드'만 가능합니다.");
	}
}
