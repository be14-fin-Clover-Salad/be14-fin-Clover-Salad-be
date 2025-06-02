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

		Customer updated = Customer.builder().id(existing.getId())
				.name(customerDTO.getName() != null ? customerDTO.getName() : existing.getName())
				.birthdate(customerDTO.getBirthdate() != null ? customerDTO.getBirthdate()
						: existing.getBirthdate())
				.phone(customerDTO.getPhone() != null ? customerDTO.getPhone()
						: existing.getPhone())
				.address(customerDTO.getAddress() != null ? customerDTO.getAddress()
						: existing.getAddress())
				.email(customerDTO.getEmail() != null ? customerDTO.getEmail()
						: existing.getEmail())
				.registerAt(existing.getRegisterAt()).isDeleted(existing.isDeleted())
				.type(customerDTO.getType() != null ? customerDTO.getType() : existing.getType())
				.etc(customerDTO.getEtc() != null ? customerDTO.getEtc() : existing.getEtc())
				.build();

		customerRepository.save(updated);
		log.info("Updated customer: {}", updated.getName());
	}

	@Override
	public void deleteCustomer(int customerId) {
		Customer customer = customerRepository.findById(customerId).orElseThrow(
				() -> new RuntimeException("Customer with id " + customerId + " not found"));

		Customer deletedCustomer = Customer.builder().id(customer.getId()).name(customer.getName())
				.birthdate(customer.getBirthdate()).phone(customer.getPhone())
				.address(customer.getAddress()).email(customer.getEmail())
				.registerAt(customer.getRegisterAt()).isDeleted(true).type(customer.getType())
				.etc(customer.getEtc()).build();

		customerRepository.save(deletedCustomer);
		log.info("Soft deleted customer: {}", deletedCustomer.getName());
	}

	private Customer convertToEntity(CustomerDTO dto) {
		return Customer.builder().id(dto.getId()).name(dto.getName()).birthdate(dto.getBirthdate())
				.phone(dto.getPhone()).address(dto.getAddress()).email(dto.getEmail())
				.isDeleted(dto.isDeleted()).type(dto.getType()).etc(dto.getEtc()).build();
	}
}
