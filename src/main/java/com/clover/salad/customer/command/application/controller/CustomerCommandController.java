package com.clover.salad.customer.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.dto.CustomerUpdateRequest;
import com.clover.salad.customer.command.application.service.CustomerCommandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerCommandController {

	private final CustomerCommandService service;

	// 고객 등록
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody @Valid CustomerCreateRequest dto) {
		service.registerCustomer(dto);
		return ResponseEntity.ok(dto.getName() + " 등록 완료");
	}

	// 고객 수정
	@PatchMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable int id,
			@RequestBody @Valid CustomerUpdateRequest dto) {
		service.updateCustomer(id, dto);
		return ResponseEntity.ok("고객 정보 수정 완료");
	}

	// 고객 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable int id) {
		service.deleteCustomer(id);
		return ResponseEntity.ok("고객 삭제 완료");
	}
}
