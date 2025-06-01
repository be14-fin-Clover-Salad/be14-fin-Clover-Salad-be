package com.clover.salad.customer.command.application.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.clover.salad.customer.command.application.dto.CustomerDTO;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerCommandServiceImpl implements CustomerCommandService {

	private final CustomerRepository customerRepository;

	@Override
	public void registerCustomer(CustomerDTO customerDTO) {
		Customer customer = convertToEntity(customerDTO);
		customer.setRegisterAt(LocalDate.now());
		customerRepository.save(customer);
		log.info("Registered customer: {}", customer.getName());
	}

	@Override
	public void updateCustomer(int customerId, CustomerDTO customerDTO) {
		Customer existing = customerRepository.findById(customerId).orElseThrow(
				() -> new RuntimeException("Customer with id " + customerId + " not found"));
		// 추후 CustomerNotFoundException 등으로 대체 가능

		existing.setName(customerDTO.getName());
		existing.setBirthdate(customerDTO.getBirthdate());
		existing.setPhone(customerDTO.getPhone());
		existing.setEmail(customerDTO.getEmail());
		existing.setType(customerDTO.getType());
		existing.setEtc(customerDTO.getEtc());

		customerRepository.save(existing);
		log.info("Updated customer: {}", existing.getName());
	}

	@Override
	public void deleteCustomer(int customerId) {
		Customer customer = customerRepository.findById(customerId).orElseThrow(
				() -> new RuntimeException("Customer with id " + customerId + " not found"));

		customer.setDeleted(true);
		customerRepository.save(customer);
		log.info("Soft deleted customer: {}", customer.getName());
	}

	private Customer convertToEntity(CustomerDTO dto) {
		return Customer.builder().id(dto.getId()).name(dto.getName()).birthdate(dto.getBirthdate())
				.phone(dto.getPhone()).email(dto.getEmail()).deleted(dto.isDeleted())
				.type(dto.getType()).etc(dto.getEtc()).build();
	}
}
