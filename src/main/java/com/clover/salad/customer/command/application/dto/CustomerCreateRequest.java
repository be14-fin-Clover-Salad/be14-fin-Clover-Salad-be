package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validation.ValidEmail;
import com.clover.salad.common.validation.ValidPhone;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerCreateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "생년월일은 필수입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식은 yyyy-MM-dd여야 합니다.")
    private String birthdate;

    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @ValidPhone(message = "유효하지 않은 연락처 형식입니다.")
    private String phone;

    private String address;

    @ValidEmail(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotNull(message = "고객 유형은 필수입니다.")
    private CustomerType type;

    private String etc;

    /** 엔티티 변환 (등록일은 @PrePersist로 처리) */
    public Customer toEntity() {
        return Customer.builder().name(name).birthdate(birthdate).phone(phone).address(address)
                .email(email).type(type).etc(etc).build();
    }

    /** 이름, 생년월일, 휴대폰 제외한 필드만 추출 */
    public CustomerUpdateRequest toUpdateRequest() {
        return new CustomerUpdateRequest(null, null, null, address, email, type, etc);
    }
}
