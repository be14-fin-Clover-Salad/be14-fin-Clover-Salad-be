package com.clover.salad.consult.command.application.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.common.util.AuthUtil;
import com.clover.salad.consult.command.application.dto.ConsultationCreateRequest;
import com.clover.salad.consult.command.domain.aggregate.entity.Consultation;
import com.clover.salad.consult.command.domain.repository.ConsultationRepository;
import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.service.CustomerCommandService;
import com.clover.salad.customer.query.service.CustomerQueryService;
import com.clover.salad.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationCommandServiceImpl implements ConsultationCommandService {

    private final ConsultationRepository consultationRepository;
    private final CustomerCommandService customerCommandService;
    private final CustomerQueryService customerQueryService;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void createConsultation(ConsultationCreateRequest request) {
        String token = AuthUtil.resolveToken();
        Integer employeeId = jwtUtil.getEmployeeId(token);

        String name = request.getCustomerName();
        String phone = request.getCustomerPhone();
        String birth = request.getCustomerBirthdate();

        if (!request.hasAnyCustomerIdentifier()) {
            throw new IllegalArgumentException("고객 이름, 연락처, 생년월일 중 최소 하나는 반드시 입력해야 합니다.");
        }

        // 고객 등록 or 병합 처리 (도메인 정책에 따라 내부에서 판별 및 처리)
        CustomerCreateRequest customerRequest = CustomerCreateRequest.builder().name(name)
                .phone(phone).birthdate(birth).address(request.getCustomerAddress())
                .email(request.getCustomerEmail()).etc(request.getCustomerEtc()).build();

        customerCommandService.registerCustomer(customerRequest);

        // 등록/병합 이후 고객 ID 조회 (is_deleted = false 기준)
        Integer customerId = customerQueryService.findRegisteredCustomerId(name, birth, phone);

        if (Objects.isNull(customerId)) {
            throw new IllegalStateException("고객 정보 등록 후에도 ID를 찾을 수 없습니다.");
        }

        // 상담 등록
        Consultation consultation = Consultation.builder().content(request.getContent())
                .etc(request.getEtc()).employeeId(employeeId).customerId(customerId).build();

        consultationRepository.save(consultation);
    }
}
