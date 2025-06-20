package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidEmail;
import com.clover.salad.common.validator.ValidPhone;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ValidBirthdate(message = "유효하지 않은 날짜 형식입니다.")
    private String birthdate;

    @ValidPhone(message = "유효하지 않은 연락처 형식입니다.")
    private String phone;

    private String address;

    @ValidEmail(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @JsonProperty("type")
    private CustomerType type;

    private String etc;

    /** 부분 수정용 엔티티 변환 */
    public Customer toEntity(CustomerType resolvedType) {
        return Customer.builder().name(name).birthdate(birthdate)
                .phone(phone != null ? phone.replaceAll("-", "") : null).address(address)
                .email(email).type(resolvedType).etc(etc).build();
    }
}
