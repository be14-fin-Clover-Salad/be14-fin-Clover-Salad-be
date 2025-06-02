package com.clover.salad.contract.command.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.command.entity.ContractEntity;
import com.clover.salad.contract.command.repository.ContractCustomerRepository;
import com.clover.salad.contract.command.repository.ContractRepository;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {

	private final ContractRepository contractRepository;
	private final ContractCustomerRepository contractCustomerRepository;

	@Transactional
	public ContractEntity registerContract(ContractUploadRequestDTO dto) {
		Customer customer = contractCustomerRepository.save(dto.getCustomer().toEntityWithDefaults());

		String generatedCode = generateContractCode(); // 예: "C202505280001"

		ContractEntity contract = dto.getContract().toEntityWithDefaults(
			customer,
			generatedCode,
			dto.getDocumentOrigin()
		);

		return contractRepository.save(contract);
	}

	private String generateContractCode() {
		String prefix = "C";
		String datePart = java.time.LocalDate.now().toString().replace("-", "");
		String seq = String.format("%04d", contractRepository.count() + 1); // 단순 예시
		return prefix + datePart + seq;
	}
}
