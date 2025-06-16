package com.clover.salad.customer.query.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.common.exception.CustomersException;
import com.clover.salad.customer.query.dto.CustomerQueryDTO;
import com.clover.salad.customer.query.service.CustomerQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerQueryController {

    private final CustomerQueryService customerQueryService;

    @GetMapping
    public ResponseEntity<List<CustomerQueryDTO>> findAll() {
        return ResponseEntity.ok(customerQueryService.findAll());
    }

    /** 설명. 특정 사원(employeeId에 해당하는)이 담당하는 고객 목록(다중 건) 조회 */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getCustomersByEmployeeId(@PathVariable int employeeId) {
        try {
            List<CustomerQueryDTO> customers =
                    customerQueryService.findCustomersByEmployeeId(employeeId);
            return ResponseEntity.ok(customers);
        } catch (CustomersException.CustomerNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /** 설명. 특정 사원(employeeId에 해당하는)이 담당하는 고객(customerId) 단일 건 조회 */
    @GetMapping("/{customerId}/employee/{employeeId}")
    public ResponseEntity<?> findCustomerByEmployeeAndCustomerId(@PathVariable int customerId,
            @PathVariable int employeeId) {
        try {
            CustomerQueryDTO customer = customerQueryService
                    .findCustomerByEmployeeAndCustomerId(customerId, employeeId);
            return ResponseEntity.ok(customer);
        } catch (CustomersException.CustomerAccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Integer> findRegisteredCustomerId(@RequestParam("name") String name,
            @RequestParam("birthdate") String birthdate, @RequestParam("phone") String phone) {

        Integer customerId = customerQueryService.findRegisteredCustomerId(name, birthdate, phone);
        return customerId != null ? ResponseEntity.ok(customerId) : null;
    }
}
