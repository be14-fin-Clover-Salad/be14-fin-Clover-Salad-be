package com.clover.salad.common.validation;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {

    private EmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
    }

    @Test
    void 유효한_이메일_성공() {
        assertThat(validator.isValid("user.name@example.com", null)).isTrue();
        assertThat(validator.isValid("john-doe_123@domain.co.kr", null)).isTrue();
    }

    @Test
    void 점으로_시작하거나_끝나는_이메일_실패() {
        assertThat(validator.isValid(".username@domain.com", null)).isFalse();
        assertThat(validator.isValid("username.@domain.com", null)).isFalse();
    }

    @Test
    void 연속된_점_포함된_이메일_실패() {
        assertThat(validator.isValid("user..name@domain.com", null)).isFalse();
    }

    @Test
    void 잘못된_형식의_이메일_실패() {
        assertThat(validator.isValid("plainaddress", null)).isFalse();
        assertThat(validator.isValid("user@.com", null)).isFalse();
        assertThat(validator.isValid("user@domain", null)).isFalse();
    }

    @Test
    void null_또는_빈값_이메일_실패() {
        assertThat(validator.isValid(null, null)).isFalse();
        assertThat(validator.isValid("", null)).isFalse();
        assertThat(validator.isValid(" ", null)).isFalse();
    }
}
