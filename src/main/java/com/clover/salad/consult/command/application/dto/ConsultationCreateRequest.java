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

    @NotBlank
    private String content;
    private String etc;
}
