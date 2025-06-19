package com.clover.salad.consult.command.application.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.common.util.AuthUtil;
import com.clover.salad.consult.command.application.dto.ConsultationCreateRequest;
import com.clover.salad.consult.command.domain.aggregate.entity.Consultation;
import com.clover.salad.consult.command.domain.repository.ConsultationRepository;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.repository.CustomerRepository;
import com.clover.salad.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationCommandServiceImpl implements ConsultationCommandService {

    private final ConsultationRepository consultationRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void createConsultation(ConsultationCreateRequest request) {
        String token = AuthUtil.resolveToken();
        Integer employeeId = jwtUtil.getEmployeeId(token);

        String name = request.getCustomerName();
        String phone = request.getCustomerPhone();
        String birth = request.getCustomerBirthdate();

        // 이름과 연락처 중 하나는 반드시 존재해야 함
        if ((name == null || name.isBlank()) && (phone == null || phone.isBlank())) {
            throw new IllegalArgumentException("고객 이름 또는 연락처 중 하나는 반드시 입력해야 합니다.");
        }

        Customer customer;

        // 고객 매핑 조건: 이름 + 연락처 + (생년월일 옵션)
        Optional<Customer> matchedCustomer =
                customerRepository.findByNameAndPhoneAndBirthdateOptional(name, phone, birth);

        if (matchedCustomer.isPresent()) {
            customer = matchedCustomer.get(); // 기존 고객 사용
        } else {
            customer = Customer.builder().name(name).phone(phone).birthdate(birth)
                    .registerAt(LocalDate.now()).isDeleted(false).type("리드").build();
            customerRepository.save(customer);
        }

        // 신규 상담 등록
        Consultation consultation = Consultation.builder().content(request.getContent())
                .etc(request.getEtc()).employeeId(employeeId).customerId(customer.getId()).build();

        consultationRepository.save(consultation);
    }
}
