package com.clover.salad.customer.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/detail/{id}")
    public ResponseEntity<CustomerQueryDTO> findById(@PathVariable("id") int id) {
        CustomerQueryDTO customer = customerQueryService.findCustomerById(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerQueryDTO>> findAll() {
        return ResponseEntity.ok(customerQueryService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerQueryDTO>> findAllActive() {
        return ResponseEntity.ok(customerQueryService.findAllActive());
    }

    @GetMapping("/check")
    public ResponseEntity<Integer> findRegisteredCustomerId(@RequestParam("name") String name,
            @RequestParam("birthdate") String birthdate, @RequestParam("phone") String phone) {

        Integer customerId = customerQueryService.findRegisteredCustomerId(name, birthdate, phone);
        return customerId != null ? ResponseEntity.ok(customerId) : null;
    }
}
