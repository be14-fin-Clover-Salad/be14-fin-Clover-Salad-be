package com.clover.salad.contract.command.service;

import java.io.File;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.contract.command.dto.*;
import com.clover.salad.contract.command.entity.*;
import com.clover.salad.contract.command.repository.*;
import com.clover.salad.contract.command.service.parser.PdfContractParserService;
import com.clover.salad.contract.common.ContractStatus;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import com.clover.salad.contract.document.service.DocumentOriginService;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
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
	private final ContractCustomerRepository contractCustomerRepository;
	private final ProductRepository productRepository;
	private final ContractProductRepository contractProductRepository;
	private final ContractFileHistoryRepository contractFileHistoryRepository;
	private final DocumentOriginService documentOriginService;
	private final PdfContractParserService pdfContractParserService;

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

		String yy = String.format("%02d", now.getYear() % 100); // ex: "25"
		String mm = String.format("%02d", now.getMonthValue()); // ex: "06"
		String datePart = yy + mm;
		String seq = String.format("%04d", contractRepository.count() + 1);

		return prefix + datePart + "-" + seq;
	}



	@Transactional
	public ContractEntity markContractDeleted(int contractId) {
		ContractEntity contract = contractRepository.findById(contractId)
			.orElseThrow(() -> new IllegalArgumentException("기존 계약을 찾을 수 없습니다."));

		contract.setStatus(ContractStatus.계약무효);
		contract.setDeleted(true);
		return contract;
	}

	@Transactional
	public void saveContractHistory(ContractFileHistory history) {
		contractFileHistoryRepository.save(history);
	}

	public int getNextVersion(int contractId) {
		return contractFileHistoryRepository.findMaxVersionByContractId(contractId)
			.map(v -> v + 1)
			.orElse(1);
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
