package com.clover.salad.notification.command.domain.aggregate.converter;

import com.clover.salad.notification.command.domain.aggregate.enums.NotificationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NotificationTypeConverter implements AttributeConverter<NotificationType, String> {

	@Override
	public String convertToDatabaseColumn(NotificationType attribute) {
		return attribute != null ? attribute.getLabel() : null;
	}

	@Override
	public NotificationType convertToEntityAttribute(String dbData) {
		return dbData != null ? NotificationType.fromLabel(dbData) : null;
	}
}
