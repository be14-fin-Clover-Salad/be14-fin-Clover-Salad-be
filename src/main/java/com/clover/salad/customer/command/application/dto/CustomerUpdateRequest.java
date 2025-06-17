package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validation.ValidEmail;
import com.clover.salad.common.validation.ValidPhone;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerUpdateRequest {

    private String name;

    private String birthdate;

    @ValidPhone(message = "유효하지 않은 연락처 형식입니다.")
    private String phone;

    private String address;

    @ValidEmail(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    private CustomerType type;

    private String etc;

    /** 부분 수정용 엔티티 변환 */
    public Customer toEntity() {
        return Customer.builder().name(name).birthdate(birthdate).phone(phone).address(address)
                .email(email).type(type).etc(etc).build();
    }
}
