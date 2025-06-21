package com.clover.salad.contract.command.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.dto.ContractUpdateResponseDTO;
import com.clover.salad.contract.command.dto.ContractDeleteResponseDTO;
import com.clover.salad.contract.command.dto.CustomerDTO;
import com.clover.salad.contract.command.dto.ProductDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.entity.ContractFileHistory;
import com.clover.salad.contract.command.entity.ContractProductEntity;
import com.clover.salad.contract.command.repository.ContractRepository;
import com.clover.salad.contract.command.repository.ContractProductRepository;
import com.clover.salad.contract.command.repository.ContractFileHistoryRepository;
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

	private final ContractRepository            contractRepository;
	private final ProductRepository             productRepository;
	private final ContractProductRepository     contractProductRepository;
	private final ContractFileHistoryRepository contractFileHistoryRepository;
	private final CustomerCommandService        customerCommandService;
	private final CustomerRepository            customerRepository;
	private final EmployeeRepository            employeeRepository;

	@Transactional
	public ContractEntity registerContract(ContractUploadRequestDTO dto) {
		CustomerDTO c    = dto.getCustomer();
		String     name  = c.getName();
		String     birth = c.getBirthdate();
		String     phone = c.getPhone();

		// 1) 기존 고객 조회
		Customer customer = customerRepository
			.findTopByNameAndBirthdateAndPhoneOrderByRegisterAtDesc(name, birth, phone)
			.orElse(null);

		// 2) 고객 업데이트 or 신규 등록
		if (customer != null) {
			CustomerUpdateRequest upd = CustomerUpdateRequest.builder()
				.name(name).birthdate(birth).phone(phone)
				.address(c.getAddress()).email(c.getEmail())
				.type(c.getCustomerType()).etc(null)
				.build();
			customerCommandService.updateCustomer(customer.getId(), upd, true);

		} else {
			// ▶ 신규 고객은 검증만 우회하여 등록
			CustomerCreateRequest createReq = CustomerCreateRequest.builder()
				.name(name).birthdate(birth).phone(phone)
				.address(c.getAddress()).email(c.getEmail())
				.type(c.getCustomerType()).etc(null)
				.build();
			customerCommandService.registerCustomer(createReq, true);
		}

		// 3) 갱신 또는 신규 고객 재조회
		customer = customerRepository
			.findTopByNameAndBirthdateAndPhoneOrderByRegisterAtDesc(name, birth, phone)
			.orElseThrow(() -> new IllegalStateException("고객 조회 실패"));

		// 4) 계약 코드 생성
		String code = generateContractCode();
		EmployeeEntity emp = employeeRepository.findById(SecurityUtil.getEmployeeId())
			.orElseThrow(() -> new IllegalArgumentException("사원 조회 실패"));

		// 5) 계약 저장
		ContractEntity contract = dto.getContract()
			.toEntityWithDefaults(customer, code, dto.getDocumentOrigin(), emp);
		ContractEntity saved = contractRepository.save(contract);

		// 6) 상품 매핑
		for (ProductDTO pd : dto.getProducts()) {
			Product prod = productRepository.findByNameAndSerialNumber(
				pd.getProductName(), pd.getModelName()
			).orElseThrow(() -> new IllegalArgumentException("상품 없음: " + pd.getProductName()));

			ContractProductEntity cp = ContractProductEntity.builder()
				.contract(saved)
				.product(prod)
				.quantity(pd.getQuantity())
				.build();
			contractProductRepository.save(cp);
		}

		return saved;
	}

	@Transactional(readOnly = true)
	public void validate(ContractUploadRequestDTO dto) {
		// 상품 유효성만 검사
		for (ProductDTO p : dto.getProducts()) {
			productRepository.findByNameAndSerialNumber(
				p.getProductName(), p.getModelName()
			).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다: " + p.getProductName()));
		}
	}

	private String generateContractCode() {
		LocalDate now = LocalDate.now();
		String yy  = String.format("%02d", now.getYear() % 100);
		String mm  = String.format("%02d", now.getMonthValue());
		String seq = String.format("%04d", contractRepository.count() + 1);
		return "C-" + yy + mm + "-" + seq;
	}

	@Transactional
	public ContractEntity markContractDeleted(int contractId) {
		ContractEntity c = contractRepository.findById(contractId)
			.orElseThrow(() -> new IllegalArgumentException("기존 계약을 찾을 수 없습니다."));
		c.setStatus(ContractStatus.INVALID);
		c.setDeleted(true);
		return c;
	}

	@Transactional
	public void saveContractHistory(ContractFileHistory history) {
		contractFileHistoryRepository.save(history);
	}

	public int getNextVersion(int replacedContractId) {
		int rootId = findRootContractId(replacedContractId);
		return contractFileHistoryRepository
			.findMaxVersionByReplacedContractId(rootId)
			.map(v -> v + 1)
			.orElse(1);
	}

	private int findRootContractId(int contractId) {
		Optional<ContractFileHistory> h =
			contractFileHistoryRepository.findByContract_Id(contractId);
		while (h.isPresent() && h.get().getReplacedContract() != null) {
			contractId = h.get().getReplacedContract().getId();
			h = contractFileHistoryRepository.findByContract_Id(contractId);
		}
		return contractId;
	}

	@Transactional
	public ContractUpdateResponseDTO updateEtcOnly(int contractId, String etc) {
		ContractEntity c = contractRepository.findById(contractId)
			.orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));
		c.setEtc(etc);
		return new ContractUpdateResponseDTO(c.getId(), c.getEtc());
	}

	@Transactional
	public ContractDeleteResponseDTO deleteContract(int contractId) {
		ContractEntity c = contractRepository.findById(contractId)
			.orElseThrow(() -> new IllegalArgumentException("계약을 찾을 수 없습니다."));
		c.setDeleted(true);
		return new ContractDeleteResponseDTO(c.getId(), c.isDeleted());
	}
}
