package com.clover.salad.consult.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidPhone;
import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
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

    public CustomerCreateRequest toSanitizedCustomerCreateRequest() {
        String sanitizedPhone =
                this.customerPhone != null ? this.customerPhone.replaceAll("-", "") : null;

        return CustomerCreateRequest.builder().name(this.customerName)
                .birthdate(this.customerBirthdate).phone(sanitizedPhone).etc(this.etc).build();
    }

}
