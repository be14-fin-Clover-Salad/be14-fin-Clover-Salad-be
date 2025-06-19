package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidEmail;
import com.clover.salad.common.validator.ValidPhone;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerCreateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @ValidBirthdate
    private String birthdate;

    @ValidPhone(message = "유효하지 않은 연락처 형식입니다.")
    private String phone;

    private String address;

    @ValidEmail(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    private String etc;

    public Customer toEntity() {
        return Customer.builder().name(this.name).birthdate(this.birthdate).phone(this.phone)
                .email(this.email).address(this.address).etc(this.etc).build();
    }

    public CustomerUpdateRequest toUpdateRequest() {
        return CustomerUpdateRequest.builder().address(this.address).email(this.email).etc(this.etc)
                .build();
    }
}
