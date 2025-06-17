package com.clover.salad.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_REGEX =
            "^(?!\\.)(?!.*\\.\\.)[A-Za-z0-9._-]+(?<!\\.)@[A-Za-z0-9-]+(\\.[A-Za-z]{2,})+$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // PATCH 시 null 허용
        }

        String trimmed = email.trim();
        if (trimmed.isEmpty()) {
            return false; // 공백만 입력된 경우 → 실패
        }

        return trimmed.matches(EMAIL_REGEX);
    }
}
