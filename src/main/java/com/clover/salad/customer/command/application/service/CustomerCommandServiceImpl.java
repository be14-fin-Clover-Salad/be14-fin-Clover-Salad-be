package com.clover.salad.customer.command.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
		registerCustomer(request, false);
	}

	@Override
	@Transactional
	public Integer registerCustomer(CustomerCreateRequest request, boolean bypassValidation) {
		if (!isRegisterableCustomer(request.getName(), request.getPhone(),
				request.getBirthdate())) {
			throw new CustomersException.InvalidCustomerDataException(
					"이름 또는 연락처 중 하나는 반드시 입력되어야 하며, 생년월일만으로는 고객 등록이 불가능합니다.");
		}

		Integer existingCustomerId = findDuplicateCustomerId(request);

		if (existingCustomerId == null) {
			boolean hasContract = contractExists(request);
			boolean hasConsult = consultExists(request);

			if (!bypassValidation && !hasContract && !hasConsult) {
				throw new CustomersException.InvalidCustomerDataException(
						"계약 또는 상담 정보 없이 고객 등록은 불가능합니다.");
			}

			Customer newCustomer = request.toEntity();
			CustomerType type = determineCustomerType(hasContract, hasConsult, bypassValidation);
			newCustomer.setType(type);
			Customer saved = customerRepository.saveAndFlush(newCustomer);

			log.info("[신규 고객 등록] 이름: {}, 등록 완료", request.getName());
			return saved.getId(); // 등록된 고객 ID 직접 반환
		} else {
			Customer existingCustomer = customerRepository.findById(existingCustomerId).orElseThrow(
					() -> new CustomersException.CustomerNotFoundException("기존 고객을 찾을 수 없습니다."));

			CustomerType currentType = existingCustomer.getType();
			existingCustomer.updateFromRequest(request, currentType);

			log.info("[기존 고객 업데이트] ID: {}, 이름: {}", existingCustomerId, existingCustomer.getName());
			return existingCustomerId; // 기존 고객 ID 반환
		}
	}


	private boolean isRegisterableCustomer(String name, String phone, String birthdate) {
		boolean hasName = name != null && !name.isBlank();
		boolean hasPhone = phone != null && !phone.isBlank();
		boolean hasBirthdate = birthdate != null && !birthdate.isBlank();
		boolean hasIdentifier = hasName || hasPhone;
		boolean birthOnly = hasBirthdate && !hasIdentifier;
		return hasIdentifier && !birthOnly;
	}

	@Override
	@Transactional
	public void updateCustomer(int customerId, CustomerUpdateRequest request) {
		int loginEmployeeId = AuthUtil.getEmployeeId();
		List<Integer> accessibleCustomerIds =
				contractService.getCustomerIdsByEmployee(loginEmployeeId);

		if (!accessibleCustomerIds.contains(customerId)) {
			throw new CustomersException.CustomerAccessDeniedException("해당 고객에 대한 수정 권한이 없습니다.");
		}

		Customer customer = customerRepository.findById(customerId).orElseThrow(
				() -> new CustomersException.CustomerNotFoundException("고객이 존재하지 않습니다."));

		Customer updated = request.toEntity(request.getType());
		customer.update(updated);

		log.info("[고객 수정] ID: {}, 이름: {}", customerId, customer.getName());
	}

	@Transactional
	@Override
	public void updateCustomer(int customerId, CustomerUpdateRequest request, boolean bypassValidation) {
		if (!bypassValidation) {
			// 기존 권한 체크
			int loginEmployeeId = AuthUtil.getEmployeeId();
			List<Integer> accessible = contractService.getCustomerIdsByEmployee(loginEmployeeId);
			if (!accessible.contains(customerId)) {
				throw new CustomersException.CustomerAccessDeniedException("수정 권한이 없습니다.");
			}
		}
		Customer customer = customerRepository.findById(customerId)
			.orElseThrow(() -> new CustomersException.CustomerNotFoundException("고객이 없습니다."));
		Customer updated = request.toEntity(request.getType());
		customer.update(updated);
		log.info("[고객 수정{}] ID: {}, 이름: {}",
			(bypassValidation ? " (bypass)" : ""), customerId, customer.getName());
	}

	@Transactional(readOnly = true)
	public Integer findDuplicateCustomerId(CustomerCreateRequest request) {
		String normalizedPhone =
				request.getPhone() != null ? request.getPhone().replaceAll("-", "") : null;

		return customerQueryService.findRegisteredCustomerId(request.getName(),
				request.getBirthdate(), normalizedPhone);
	}

	private CustomerType determineCustomerType(boolean hasContract, boolean hasConsult,
			boolean bypassValidation) {
		if (hasContract)
			return CustomerType.CUSTOMER;
		if (hasConsult)
			return CustomerType.PROSPECT;
		if (bypassValidation)
			return CustomerType.PROSPECT;
		throw new CustomersException.InvalidCustomerDataException(
				"계약 또는 상담 이력이 없는 고객은 등록할 수 없습니다.");
	}

	private boolean contractExists(CustomerCreateRequest request) {
		return customerQueryService.existsContractByCustomer(request.getName(),
				request.getBirthdate(),
				request.getPhone() != null ? request.getPhone().replaceAll("-", "") : null);
	}

	private boolean consultExists(CustomerCreateRequest request) {
		return customerQueryService.existsConsultByCustomer(request.getName(),
				request.getBirthdate(),
				request.getPhone() != null ? request.getPhone().replaceAll("-", "") : null);
	}
}
