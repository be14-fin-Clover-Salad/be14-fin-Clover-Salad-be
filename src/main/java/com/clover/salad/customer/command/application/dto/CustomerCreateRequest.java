package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidEmail;
import com.clover.salad.common.validator.ValidPhone;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerCreateRequest {

    private String name;

    @ValidBirthdate
    private String birthdate;

    @ValidPhone
    private String phone;

    private String address;

    @ValidEmail
    private String email;

    private String etc;

    public Customer toEntity() {
        return Customer.builder().name(this.name).birthdate(this.birthdate).phone(this.phone)
                .email(this.email).address(this.address).etc(this.etc).build();
    }

    public boolean hasAnyCustomerIdentifier() {
        return (name != null && !name.isBlank()) || (phone != null && !phone.isBlank())
                || (birthdate != null && !birthdate.isBlank());
    }
}
