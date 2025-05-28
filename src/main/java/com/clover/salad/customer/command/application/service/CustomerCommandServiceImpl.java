package com.clover.salad.customer.command.application.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	private final CustomerRepository customerRep;

	@Override
	public void registerCustomer(CustomerDTO customerDTO) {
		customerRep.save(customerDTOToCustomer(customerDTO));
	}


	private Customer customerDTOToCustomer(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setId(customerDTO.getId());
		customer.setName(customerDTO.getName());
		customer.setBirthdate(customerDTO.getBirthdate());
		customer.setPhone(customerDTO.getPhone());
		customer.setEmail(customerDTO.getEmail());

		// registerAt: String → LocalDateTime 변환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime registerAt = LocalDateTime.parse(customerDTO.getRegisterAt(), formatter);
		customer.setRegisterAt(registerAt);

		customer.setDeleted(customerDTO.isDeleted()); // boolean 필드
		customer.setType(customerDTO.getType());
		customer.setEtc(customerDTO.getEtc());

		return customer;
	}
}
