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
import com.clover.salad.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationCommandServiceImpl implements ConsultationCommandService {

    private final ConsultationRepository consultationRepository;
    private final CustomerCommandService customerCommandService;
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

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
