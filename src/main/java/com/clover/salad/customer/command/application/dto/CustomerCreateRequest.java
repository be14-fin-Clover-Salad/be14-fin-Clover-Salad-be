package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidEmail;
import com.clover.salad.common.validator.ValidPhone;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
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
public class CustomerCreateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @ValidBirthdate
    private String birthdate;

    @ValidPhone
    private String phone;

    private String address;

    @ValidEmail
    private String email;

    @NotBlank(message = "고객 유형은 필수입니다.")
    @Pattern(regexp = "^(리드|고객)$", message = "고객 유형은 '리드' 또는 '고객'이어야 합니다.")
    private String type;

    private String etc;

    public Customer toEntity() {
        return Customer.builder().name(this.name).birthdate(this.birthdate).phone(this.phone)
                .email(this.email).address(this.address).type(CustomerType.from(this.type))
                .etc(this.etc).build();
    }

    public CustomerUpdateRequest toUpdateRequest() {
        return CustomerUpdateRequest.builder().address(this.address).email(this.email)
                .type(CustomerType.from(this.type)).etc(this.etc).build();
    }
}
