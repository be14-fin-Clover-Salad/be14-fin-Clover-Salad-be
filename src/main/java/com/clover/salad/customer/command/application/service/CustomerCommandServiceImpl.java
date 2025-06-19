package com.clover.salad.customer.command.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.common.exception.CustomersException;
import com.clover.salad.common.util.AuthUtil;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.dto.CustomerUpdateRequest;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.aggregate.vo.CustomerType;
import com.clover.salad.customer.command.domain.repository.CustomerRepository;
import com.clover.salad.customer.query.service.CustomerQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerCommandServiceImpl implements CustomerCommandService {

	private final CustomerRepository customerRepository;
	private final CustomerQueryService customerQueryService;
	private final ContractService contractService;

	@Override
	@Transactional
	public void registerCustomer(CustomerCreateRequest request) {
		// 1. 중복 고객 ID 조회 (is_deleted = false 조건 포함)
		Integer existingCustomerId = findDuplicateCustomerId(request);

		if (existingCustomerId == null) {
			// 2. 신규 등록
			Customer newCustomer = request.toEntity();
			CustomerType type = determineCustomerType(request.getName(), request.getBirthdate(),
					request.getPhone());
			newCustomer.setType(type);
			customerRepository.save(newCustomer);
			log.info("[신규 고객 등록] 이름: {}, 등록 완료", request.getName());

		} else {
			// 3. 기존 고객 업데이트 (병합 처리)
			Customer existingCustomer = customerRepository.findById(existingCustomerId).orElseThrow(
					() -> new CustomersException.CustomerNotFoundException("기존 고객을 찾을 수 없습니다."));

			CustomerUpdateRequest updateRequest = request.toUpdateRequest(); // 일부 필드만 포함됨
			Customer updated = updateRequest.toEntity(existingCustomer.getType()); // 기존 타입 유지
			existingCustomer.update(updated); // null 값 무시하고 병합
			log.info("[기존 고객 업데이트] ID: {}, 이름: {}", existingCustomerId, existingCustomer.getName());
		}
	}

	@Override
	@Transactional
	public void updateCustomer(int customerId, CustomerUpdateRequest request) {
		// 1. 로그인 사용자 정보 (TokenPrincipal 기반)
		int loginEmployeeId = AuthUtil.getEmployeeId();

		// 2. 권한 검증: 계약 정보 기반 고객 수정 가능 여부 확인
		List<Integer> accessibleCustomerIds =
				contractService.getCustomerIdsByEmployee(loginEmployeeId);
		if (!accessibleCustomerIds.contains(customerId)) {
			throw new CustomersException.CustomerAccessDeniedException("해당 고객에 대한 수정 권한이 없습니다.");
		}

		// 3. 고객 조회 및 업데이트
		Customer customer = customerRepository.findById(customerId).orElseThrow(
				() -> new CustomersException.CustomerNotFoundException("고객이 존재하지 않습니다."));

		Customer updated = request.toEntity(customer.getType()); // 기존 타입 유지
		customer.update(updated);

		log.info("[고객 수정] ID: {}, 이름: {}", customerId, customer.getName());
	}

	/** 중복 고객 조회 (is_deleted = false 조건 적용) */
	@Transactional(readOnly = true)
	public Integer findDuplicateCustomerId(CustomerCreateRequest request) {
		return customerQueryService.findRegisteredCustomerId(request.getName(),
				request.getBirthdate(), request.getPhone());
	}

	/** 고객 유형 자동 판별 (계약 or 상담 존재 여부 기준) */
	private CustomerType determineCustomerType(String name, String birthdate, String phone) {
		// boolean hasContract = contractService.existsByCustomer(name, birthdate, phone);
		// boolean hasConsult = consultQueryService.existsByCustomer(name, birthdate, phone);

		// if (hasContract) return CustomerType.CUSTOMER;
		// if (hasConsult) return CustomerType.PROSPECT;

		throw new CustomersException.InvalidCustomerDataException(
				"계약 또는 상담 이력이 없는 고객은 등록할 수 없습니다.");
	}
}
