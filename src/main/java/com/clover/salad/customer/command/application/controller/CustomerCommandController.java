package com.clover.salad.customer.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.customer.command.application.dto.CustomerDTO;
import com.clover.salad.customer.command.application.service.CustomerCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customer")
@Slf4j
@RequiredArgsConstructor
public class CustomerCommandController {
	private final CustomerCommandService customerCSvc;

	/* 고객 등록 */
	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody CustomerDTO customerDTO) {
		customerCSvc.registerCustomer(customerDTO);
		return ResponseEntity.ok(customerDTO.getName() + " is registered");
	}

	/* 고객 수정 */
	@PutMapping("/update/{customerId}")
	public ResponseEntity<String> updateCustomer(@PathVariable int customerId,
			@RequestBody CustomerDTO customerDTO) {
		customerCSvc.updateCustomer(customerId, customerDTO);
		return ResponseEntity.ok(customerDTO.getName() + " is updated");
	}

	/* 고객 삭제 (Soft Delete) */
	@DeleteMapping("/delete/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable int customerId) {
		customerCSvc.deleteCustomer(customerId);
		return ResponseEntity.ok("Customer " + customerId + " is deleted");
	}
}
