package com.clover.salad.customer.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerUpdateRequest {
    private String name;
    private String birthdate;
    private String phone;
    private String address;
    private String email;
    private String type;
    private String etc;
}
