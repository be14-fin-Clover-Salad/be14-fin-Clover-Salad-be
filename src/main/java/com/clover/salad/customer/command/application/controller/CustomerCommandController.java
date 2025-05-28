package com.clover.salad.customer.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
	
}
