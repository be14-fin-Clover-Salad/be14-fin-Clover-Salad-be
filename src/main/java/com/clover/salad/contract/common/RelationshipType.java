package com.clover.salad.contract.common;

import java.util.Arrays;

public enum RelationshipType {
	본인, 부모, 자녀;

	public static RelationshipType from(String value) {
		return Arrays.stream(values())
			.filter(r -> r.name().equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 관계: " + value));
	}
}
