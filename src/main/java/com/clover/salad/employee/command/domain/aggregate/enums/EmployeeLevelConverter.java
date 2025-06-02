package com.clover.salad.employee.command.domain.aggregate.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EmployeeLevelConverter implements AttributeConverter<EmployeeLevel, String> {

	@Override
	public String convertToDatabaseColumn(EmployeeLevel attribute) {
		if (attribute == null)
			return null;
		return attribute.getLabel();
	}

	@Override
	public EmployeeLevel convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;
		try {
			return EmployeeLevel.fromLabel(dbData);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("확인할 수 없는 직급입니다. 입력된 직급명: " + dbData);
		}
	}
}