package com.clover.salad.customer.command.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clover.salad.customer.command.application.dto.CustomerDTO;
import com.clover.salad.customer.command.application.service.CustomerCommandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customer")
@Slf4j
@RequiredArgsConstructor
public class CustomerCommandController {

	private final CustomerCommandService customerCommandService;

	/** 고객 등록 */
	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
		try {
			customerCommandService.registerCustomer(customerDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO.getName() + " 등록 완료");
		} catch (Exception e) {
			log.error("고객 등록 실패", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("고객 등록 중 오류 발생");
		}
	}

	/** 고객 수정 */
	@PutMapping("/update/{customerId}")
	public ResponseEntity<String> updateCustomer(@PathVariable int customerId,
			@RequestBody CustomerDTO customerDTO) {
		try {
			customerCommandService.updateCustomer(customerId, customerDTO);
			return ResponseEntity.ok(customerDTO.getName() + " 수정 완료");
		} catch (RuntimeException e) {
			log.warn("수정 대상 고객 없음: {}", customerId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 고객이 존재하지 않습니다.");
		} catch (Exception e) {
			log.error("고객 수정 실패", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("고객 수정 중 오류 발생");
		}
	}

	/** 고객 삭제 (Soft Delete) */
	@DeleteMapping("/delete/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable int customerId) {
		try {
			customerCommandService.deleteCustomer(customerId);
			return ResponseEntity.ok("고객 " + customerId + " 삭제 완료");
		} catch (RuntimeException e) {
			log.warn("삭제 대상 고객 없음: {}", customerId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 고객이 존재하지 않습니다.");
		} catch (Exception e) {
			log.error("고객 삭제 실패", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("고객 삭제 중 오류 발생");
		}
	}
}
