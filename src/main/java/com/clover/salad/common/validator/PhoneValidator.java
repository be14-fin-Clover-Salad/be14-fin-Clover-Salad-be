package com.clover.salad.common.validator;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(010|011|016|017|018|019)\\d{7,8}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String sanitized = value.replaceAll("-", "");

        return PHONE_PATTERN.matcher(sanitized).matches();
    }
}
