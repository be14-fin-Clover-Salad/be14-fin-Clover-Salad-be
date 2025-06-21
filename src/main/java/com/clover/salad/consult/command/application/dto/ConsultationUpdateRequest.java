package com.clover.salad.consult.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidEmail;
import com.clover.salad.common.validator.ValidPhone;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ConsultationUpdateRequest {

    private String customerName;

    @ValidBirthdate
    private String customerBirthdate;

    @ValidPhone
    private String customerPhone;

    @ValidEmail
    private String customerEmail;

    private String customerAddress;

    @JsonProperty("type")
    private CustomerType customerType;

    private String customerEtc;

    @NotBlank(message = "상담 내용은 필수입니다.")
    private String content;

    private String etc;

}
