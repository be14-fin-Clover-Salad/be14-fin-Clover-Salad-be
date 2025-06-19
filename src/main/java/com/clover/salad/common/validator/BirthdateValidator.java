package com.clover.salad.common.validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthdateValidator implements ConstraintValidator<ValidBirthdate, String> {

    // yyyy-MM-dd 형식
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 또는 빈 문자열은 허용 (Optional 필드)
        if (value == null || value.isBlank())
            return true;

        // 형식 확인 (yyyy-MM-dd)
        if (!DATE_PATTERN.matcher(value).matches()) {
            return false;
        }

        // 날짜 파싱 및 미래 날짜 방지
        try {
            LocalDate birthdate = LocalDate.parse(value);
            return !birthdate.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
