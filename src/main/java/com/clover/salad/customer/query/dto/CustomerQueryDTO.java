package com.clover.salad.customer.query.dto;

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
    private String registerAt;
    private boolean isDeleted;
    private String type;
    private String etc;
}
