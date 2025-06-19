package com.clover.salad.consult.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidPhone;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ConsultationCreateRequest {

    private String customerName;

    @ValidBirthdate
    private String customerBirthdate;

    @ValidPhone
    private String customerPhone;

    @NotBlank(message = "상담 내용은 필수입니다.")
    private String content;

    private String etc;

    /** 필수 고객 식별 정보가 존재하는지 확인 */
    public boolean hasAnyCustomerIdentifier() {
        return (customerName != null && !customerName.isBlank())
                || (customerPhone != null && !customerPhone.isBlank())
                || (customerBirthdate != null && !customerBirthdate.isBlank());
    }
}
