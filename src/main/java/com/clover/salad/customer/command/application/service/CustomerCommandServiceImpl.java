package com.clover.salad.customer.command.application.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.dto.CustomerUpdateRequest;
import com.clover.salad.customer.command.domain.aggregate.entity.Customer;
import com.clover.salad.customer.command.domain.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerCommandServiceImpl implements CustomerCommandService {

	private final CustomerRepository customerRepository;

	@Override
	@Transactional
	public void registerCustomer(CustomerCreateRequest request) {
		Customer customer = Customer.builder().name(request.getName())
				.birthdate(request.getBirthdate()).phone(request.getPhone())
				.address(request.getAddress()).email(request.getEmail()).type(request.getType())
				.etc(request.getEtc()).registerAt(LocalDate.now()) // 등록일은 현재 날짜 고정
				.isDeleted(false).build();

		customerRepository.save(customer);
	}

	@Override
	@Transactional
	public void updateCustomer(int id, CustomerUpdateRequest request) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 ID의 고객이 존재하지 않습니다."));

		Customer updated = customer.toBuilder().name(request.getName())
				.birthdate(request.getBirthdate()).phone(request.getPhone())
				.address(request.getAddress()).email(request.getEmail()).type(request.getType())
				.etc(request.getEtc()).build();

		customer.update(updated); // null 체크 병합 로직 내장
	}

	@Override
	@Transactional
	public void deleteCustomer(int id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 ID의 고객이 존재하지 않습니다."));

		customer.softDelete(); // 소프트 삭제
	}
}
