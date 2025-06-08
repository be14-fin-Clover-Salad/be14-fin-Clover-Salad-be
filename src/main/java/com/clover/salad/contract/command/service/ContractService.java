package com.clover.salad.contract.command.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.contract.command.dto.ContractDeleteResponseDTO;
import com.clover.salad.contract.command.dto.ContractUpdateResponseDTO;
import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.dto.ProductDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.entity.ContractProductEntity;
import com.clover.salad.contract.command.repository.ContractCustomerRepository;
import com.clover.salad.contract.command.repository.ContractProductRepository;
import com.clover.salad.contract.command.repository.ContractRepository;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.product.command.domain.aggregate.entity.Product;
import com.clover.salad.product.command.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {

	private final ContractRepository contractRepository;
	private final ContractCustomerRepository contractCustomerRepository;
	private final ProductRepository productRepository;
	private final ContractProductRepository contractProductRepository;

	@Transactional
	public ContractEntity registerContract(ContractUploadRequestDTO dto) {
		Customer customer = contractCustomerRepository.save(dto.getCustomer().toEntityWithDefaults());
		String generatedCode = generateContractCode();

		ContractEntity contract = dto.getContract().toEntityWithDefaults(
			customer,
			generatedCode,
			dto.getDocumentOrigin()
		);

		ContractEntity savedContract = contractRepository.save(contract);

		for (ProductDTO productDto : dto.getProducts()) {
			Product product = productRepository.findByNameAndSerialNumber(
				productDto.getProductName(),
				productDto.getModelName()
			).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

			ContractProductEntity contractProduct = ContractProductEntity.builder()
				.contract(savedContract)
				.product(product)
				.quantity(productDto.getQuantity())
				.build();

			contractProductRepository.save(contractProduct);
		}

		return savedContract;
	}


	private String generateContractCode() {
		String prefix = "C-";
		java.time.LocalDate now = java.time.LocalDate.now();

		// 연도 앞 두 자리, 월
		String yy = String.format("%02d", now.getYear() % 100); // "25"
		String mm = String.format("%02d", now.getMonthValue()); // "06"

		String datePart = yy + mm; // "2506"
		String seq = String.format("%04d", contractRepository.count() + 1); // "0001" ~ "9999"

		return prefix + datePart + "-" + seq; // C-2506-0001
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
