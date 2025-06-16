package com.clover.salad.notification.command.domain.aggregate.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum NotificationType {

	APPROVAL("결재"),
	NOTICE("공지사항"),
	QNA("문의사항");

	private final String label;

	@JsonValue
	public String getLabel() {
		return label;
	}

	@JsonCreator
	public static NotificationType fromLabel(String label) {
		if (label == null) return null;
		for (NotificationType type : NotificationType.values()) {
			if (type.label.equals(label) || type.name().equals(label)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown NotificationType label: " + label);
	}
}
