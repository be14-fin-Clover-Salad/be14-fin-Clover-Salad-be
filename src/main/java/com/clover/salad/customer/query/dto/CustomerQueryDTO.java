package com.clover.salad.customer.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerQueryDTO {
    private int id;
    private String name;
    private String birthdate;
    private String phone;
    private String address;
    private String email;
    @JsonProperty("registerAt")
    private String registerAt;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
    private String type;
    private String etc;
}
