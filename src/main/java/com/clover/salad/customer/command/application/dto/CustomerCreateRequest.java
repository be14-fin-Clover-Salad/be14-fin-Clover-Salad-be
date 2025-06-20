package com.clover.salad.customer.command.application.dto;

import com.clover.salad.common.validator.ValidBirthdate;
import com.clover.salad.common.validator.ValidEmail;
import com.clover.salad.common.validator.ValidPhone;
import com.clover.salad.consult.command.application.dto.ConsultationCreateRequest;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerCreateRequest {

    private String name;

    @ValidBirthdate
    private String birthdate;

    @ValidPhone
    private String phone;

    private String address;

    @ValidEmail
    private String email;

    private CustomerType type;

    private String etc;

    /** 엔티티 변환 시 phone 정규화 포함 */
    public Customer toEntity() {
        String sanitizedPhone = this.phone != null ? this.phone.replaceAll("-", "") : null;

        return Customer.builder().name(this.name).birthdate(this.birthdate).phone(sanitizedPhone)
                .email(this.email).address(this.address).etc(this.etc).build();
    }

    /** 고객 식별 정보 존재 여부 확인 */
    public boolean hasAnyCustomerIdentifier() {
        return (name != null && !name.isBlank()) || (phone != null && !phone.isBlank())
                || (birthdate != null && !birthdate.isBlank());
    }

    /** 상담 요청으로부터 Customer 등록용 DTO 생성 */
    public static CustomerCreateRequest from(ConsultationCreateRequest request) {
        return CustomerCreateRequest.builder().name(request.getCustomerName())
                .birthdate(request.getCustomerBirthdate()).phone(request.getCustomerPhone())
                .etc(request.getEtc()).build();
    }

    /** 연락처를 정규화한 DTO 반환 */
    public static CustomerCreateRequest fromSanitized(ConsultationCreateRequest request) {
        String normalizedPhone =
                request.getCustomerPhone() != null ? request.getCustomerPhone().replaceAll("-", "")
                        : null;

        return CustomerCreateRequest.builder().name(request.getCustomerName())
                .birthdate(request.getCustomerBirthdate()).phone(normalizedPhone).address(null)
                .email(null).etc(request.getEtc()).build();
    }
}
