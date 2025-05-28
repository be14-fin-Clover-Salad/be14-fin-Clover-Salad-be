package com.clover.salad.customer.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.customer.command.application.dto.CustomerDTO;
import com.clover.salad.customer.query.service.CustomerQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customer")
@Slf4j
@RequiredArgsConstructor
public class CustomerQueryController {
    private final CustomerQueryService customerQueryService;

    /**
     * 고객 ID로 고객 정보를 조회합니다.
     *
     * @param customerId 조회할 고객 ID
     * @return 고객 정보 DTO
     */
    @GetMapping("/detail/{customerId}")
    public ResponseEntity<CustomerDTO> findCustomerById(
            @PathVariable("customerId") int customerId) {
        log.info("고객 상세 조회 요청: id = {}", customerId);
        CustomerDTO customer = customerQueryService.findCustomerById(customerId);
        if (customer == null) {
            log.warn("고객 정보를 찾을 수 없습니다. id = {}", customerId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    /**
     * 전체 고객 목록을 조회합니다.
     *
     * @return 고객 정보 리스트
     */
    @GetMapping("/list")
    public ResponseEntity<List<CustomerDTO>> findAllCustomers() {
        log.info("전체 고객 목록 조회 요청");
        return ResponseEntity.ok(customerQueryService.findAllCustomers());
    }
}
