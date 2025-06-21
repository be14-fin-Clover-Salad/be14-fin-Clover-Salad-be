package com.clover.salad.contract.command.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.contract.command.dto.*;
import com.clover.salad.contract.command.entity.*;
import com.clover.salad.contract.command.repository.*;
import com.clover.salad.contract.common.ContractStatus;
import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.dto.CustomerUpdateRequest;
import com.clover.salad.customer.command.application.service.CustomerCommandService;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.repository.CustomerRepository;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.product.command.domain.aggregate.entity.Product;
import com.clover.salad.product.command.domain.repository.ProductRepository;
import com.clover.salad.security.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

	private final ContractRepository contractRepository;
	private final ProductRepository productRepository;
	private final ContractProductRepository contractProductRepository;
	private final ContractFileHistoryRepository contractFileHistoryRepository;
	private final CustomerCommandService customerCommandService;
	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;

	@Transactional
	public ContractEntity registerContract(ContractUploadRequestDTO dto) {
		CustomerDTO customerDto = dto.getCustomer();
		String name = customerDto.getName();
		String birthdate = customerDto.getBirthdate();
		String phone = customerDto.getPhone();

		Customer customer = customerRepository
				.findTopByNameAndBirthdateAndPhoneOrderByRegisterAtDesc(name, birthdate, phone)
				.orElse(null);

		if (customer != null) {
			CustomerUpdateRequest updateRequest =
					CustomerUpdateRequest.builder().name(name).birthdate(birthdate).phone(phone)
							.address(customerDto.getAddress()).email(customerDto.getEmail())
							.type(customerDto.getCustomerType()).etc(null).build();
			customerCommandService.updateCustomer(customer.getId(), updateRequest);

			customer = customerRepository.findById(customer.getId())
					.orElseThrow(() -> new IllegalArgumentException("기존 고객 재조회 실패"));
		} else {
			CustomerCreateRequest createRequest =
					CustomerCreateRequest.builder().name(name).birthdate(birthdate).phone(phone)
							.address(customerDto.getAddress()).email(customerDto.getEmail())
							.type(customerDto.getCustomerType()).etc(null).build();
			customerCommandService.registerCustomer(createRequest);
			customerRepository.flush();
			customer = customerRepository
					.findTopByNameAndBirthdateAndPhoneOrderByRegisterAtDesc(name, birthdate, phone)
					.orElseThrow(() -> new IllegalArgumentException("신규 고객 조회 실패"));
		}

		String generatedCode = generateContractCode();
		int employeeId = SecurityUtil.getEmployeeId();
		EmployeeEntity employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new IllegalArgumentException("사원 정보 조회 실패"));

		ContractEntity contract = dto.getContract().toEntityWithDefaults(
				customer, generatedCode, dto.getDocumentOrigin(), employee
		);
		ContractEntity savedContract = contractRepository.save(contract);

		for (ProductDTO productDto : dto.getProducts()) {
			Product product = productRepository.findByNameAndSerialNumber(
					productDto.getProductName(), productDto.getModelName()
			).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + productDto.getProductName()));

			ContractProductEntity contractProduct =
					ContractProductEntity.builder().contract(savedContract).product(product)
							.quantity(productDto.getQuantity()).build();

			contractProductRepository.save(contractProduct);
		}

		return savedContract;
	}

	@Transactional(readOnly = true)
	public void validate(ContractUploadRequestDTO dto) {
		CustomerDTO customerDto = dto.getCustomer();
		String name = customerDto.getName();
		String birthdate = customerDto.getBirthdate();
		String phone = customerDto.getPhone();

		boolean customerExists = customerRepository
			.findTopByNameAndBirthdateAndPhoneOrderByRegisterAtDesc(name, birthdate, phone)
			.isPresent();

		if (!customerExists) {
			log.info("[VALIDATION] 신규 고객 등록 예정: {}", name);
		}

		for (ProductDTO productDto : dto.getProducts()) {
			productRepository.findByNameAndSerialNumber(
					productDto.getProductName(), productDto.getModelName()
			).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다: " + productDto.getProductName()));
		}
	}

	private String generateContractCode() {
		String prefix = "C-";
		LocalDate now = LocalDate.now();
		String yy = String.format("%02d", now.getYear() % 100);
		String mm = String.format("%02d", now.getMonthValue());
		String datePart = yy + mm;
		String seq = String.format("%04d", contractRepository.count() + 1);
		return prefix + datePart + "-" + seq;
	}

	@Transactional
	public ContractEntity markContractDeleted(int contractId) {
		ContractEntity contract = contractRepository.findById(contractId)
				.orElseThrow(() -> new IllegalArgumentException("기존 계약을 찾을 수 없습니다."));
		contract.setStatus(ContractStatus.INVALID);
		contract.setDeleted(true);
		return contract;
	}

	@Transactional
	public void saveContractHistory(ContractFileHistory history) {
		contractFileHistoryRepository.save(history);
	}

	public int getNextVersion(int replacedContractId) {
		int rootId = findRootContractId(replacedContractId);
		return contractFileHistoryRepository.findMaxVersionByReplacedContractId(rootId)
				.map(v -> v + 1).orElse(1);
	}

	private int findRootContractId(int contractId) {
		Optional<ContractFileHistory> historyOpt =
				contractFileHistoryRepository.findByContract_Id(contractId);
		while (historyOpt.isPresent() && historyOpt.get().getReplacedContract() != null) {
			contractId = historyOpt.get().getReplacedContract().getId();
			historyOpt = contractFileHistoryRepository.findByContract_Id(contractId);
		}
		return contractId;
	}

	@Transactional
	public ContractUpdateResponseDTO updateEtcOnly(int contractId, String etc) {
		ContractEntity contract = contractRepository.findById(contractId)
				.orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));
		contract.setEtc(etc);
		return new ContractUpdateResponseDTO(contract.getId(), contract.getEtc());
	}

	@Transactional
	public ContractDeleteResponseDTO deleteContract(int contractId) {
		ContractEntity contract = contractRepository.findById(contractId)
				.orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));
		contract.setDeleted(true);
		return new ContractDeleteResponseDTO(contract.getId(), contract.isDeleted());
	}
}
