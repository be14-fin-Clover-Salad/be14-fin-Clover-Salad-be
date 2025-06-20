package com.clover.salad.customer.command.domain.aggregate.vo;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerTypeConverter implements AttributeConverter<CustomerType, String> {

    @Override
    public String convertToDatabaseColumn(CustomerType attribute) {
        if (attribute == null)
            return null;
        return attribute.getLabel(); // "고객", "리드"
    }

    @Override
    public CustomerType convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        return CustomerType.from(dbData); // "고객" → CUSTOMER, etc.
    }
}
