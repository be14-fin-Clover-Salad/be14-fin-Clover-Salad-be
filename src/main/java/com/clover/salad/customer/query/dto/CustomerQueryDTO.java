package com.clover.salad.customer.query.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("registerAt")
    private LocalDate registerAt;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private String type;

    private String etc;
}
