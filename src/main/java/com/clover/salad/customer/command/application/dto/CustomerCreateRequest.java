package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validation.ValidEmail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerCreateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식은 yyyy-MM-dd여야 합니다.")
    private String birthdate;

    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호는 010으로 시작하며 11자리여야 합니다.")
    private String phone;

    private String address;

    @ValidEmail
    private String email;

    @NotBlank(message = "고객 유형은 필수입니다.")
    @Pattern(regexp = "^(리드|고객)$", message = "고객 유형은 '리드' 또는 '고객'이어야 합니다.")
    private String type;

    private String etc;
}
