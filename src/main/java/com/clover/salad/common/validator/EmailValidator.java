package com.clover.salad.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_REGEX =
            "^(?!\\.)(?!.*\\.\\.)[A-Za-z0-9._-]+(?<!\\.)@[A-Za-z0-9-]+(\\.[A-Za-z]{2,})+$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }
        return email.matches(EMAIL_REGEX);
    }
}
