package com.clover.salad.contract.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContractStatusConverter implements AttributeConverter<ContractStatus, String> {

	@Override
	public String convertToDatabaseColumn(ContractStatus attribute) {
		if (attribute == null)
			return null;
		return attribute.getLabel(); // 한글로 저장
	}

	@Override
	public ContractStatus convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;
		return ContractStatus.fromLabel(dbData); // 한글 → enum
	}
}
