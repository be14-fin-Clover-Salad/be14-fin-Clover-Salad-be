package com.clover.salad.approval.command.domain.aggregate.converter;

import com.clover.salad.approval.command.domain.aggregate.enums.ApprovalState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ApprovalStateConverter implements AttributeConverter<ApprovalState, String> {

	@Override
	public String convertToDatabaseColumn(ApprovalState attribute) {
		return attribute != null ? attribute.getLabel() : null;
	}

	@Override
	public ApprovalState convertToEntityAttribute(String dbData) {
		return dbData != null ? ApprovalState.fromLabel(dbData) : null;
	}
}