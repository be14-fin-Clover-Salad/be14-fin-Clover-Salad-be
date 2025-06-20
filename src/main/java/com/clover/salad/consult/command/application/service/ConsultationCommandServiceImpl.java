package com.clover.salad.consult.command.application.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.common.util.AuthUtil;
import com.clover.salad.consult.command.application.dto.ConsultationCreateRequest;
import com.clover.salad.consult.command.application.dto.ConsultationUpdateRequest;
import com.clover.salad.consult.command.domain.aggregate.entity.Consultation;
import com.clover.salad.consult.command.domain.repository.ConsultationRepository;
import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.service.CustomerCommandService;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.repository.CustomerRepository;
import com.clover.salad.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationCommandServiceImpl implements ConsultationCommandService {

    private final ConsultationRepository consultationRepository;
    private final CustomerCommandService customerCommandService;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void createConsultation(ConsultationCreateRequest request) {
        // 1. 인증된 사원 ID 확인
        String token = AuthUtil.resolveToken();
        Integer employeeId = jwtUtil.getEmployeeId(token);

        // 2. 고객 식별 정보 추출
        String name = request.getCustomerName();
        String phone = request.getCustomerPhone();

        // 3. 필수 고객 정보 확인
        if (isBlank(name) || isBlank(phone)) {
            throw new IllegalArgumentException("고객 이름과 연락처는 반드시 입력해야 합니다.");
        }

        // 4. 고객 정보 등록/병합 처리 → ID 직접 반환
        CustomerCreateRequest customerRequest = request.toSanitizedCustomerCreateRequest();
        Integer customerId = customerCommandService.registerCustomer(customerRequest, true);

        if (Objects.isNull(customerId)) {
            throw new IllegalStateException("고객 정보 등록 후에도 ID를 찾을 수 없습니다.");
        }

        // 5. 상담 정보 생성 및 저장
        Consultation consultation = Consultation.builder().content(request.getContent())
                .etc(request.getEtc()).employeeId(employeeId).customerId(customerId).build();

        consultationRepository.save(consultation);
    }

    @Override
    @Transactional
    public void updateConsultation(int consultId, ConsultationUpdateRequest request) {
        // 1. 상담 엔티티 조회 (삭제되지 않은 상담)
        Consultation consultation = consultationRepository.findByIdAndIsDeletedFalse(consultId)
                .orElseThrow(() -> new IllegalArgumentException("삭제되었거나 존재하지 않는 상담입니다."));

        // 2. 고객 엔티티 조회
        Customer customer = customerRepository.findById(consultation.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("상담에 연결된 고객 정보를 찾을 수 없습니다."));

        // 3. 상담 업데이트 (입력값이 있고, 기존 값과 다를 경우만)
        consultation.update(
                Consultation.builder().content(request.getContent()).etc(request.getEtc()).build());

        // 4. 고객 업데이트
        customer.update(Customer.builder().name(request.getCustomerName())
                .birthdate(request.getCustomerBirthdate()).phone(request.getCustomerPhone())
                .email(request.getCustomerEmail()).address(request.getCustomerAddress())
                .type(request.getCustomerType()).etc(request.getCustomerEtc()).build());
    }

    @Override
    @Transactional
    public void deleteConsultation(int consultId) {
        // 1. 삭제되지 않은 상담 조회
        Consultation consultation = consultationRepository.findByIdAndIsDeletedFalse(consultId)
                .orElseThrow(() -> new IllegalArgumentException("삭제되었거나 존재하지 않는 상담입니다."));

        // 2. 소프트 삭제 처리
        consultation.setDeleted(true);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
