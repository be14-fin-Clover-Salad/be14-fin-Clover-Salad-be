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
    private final CustomerQueryService customerQSrv;

    /* 고객 상세 조회 */
    @GetMapping("/detail/{customerId}")
    public ResponseEntity<CustomerDTO> searchCustomerById(
            @PathVariable("customerId") int customerId) {
        return ResponseEntity.ok(customerQSrv.searchCustomerById(customerId));
    }

    /* 고객 목록 조회 */
    @GetMapping("/list")
    public ResponseEntity<List<CustomerDTO>> searchCustomerList() {
        return ResponseEntity.ok(customerQSrv.searchAllCustomers());
    }
}
