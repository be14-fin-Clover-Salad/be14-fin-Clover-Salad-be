package com.clover.salad.customer.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.customer.query.dto.CustomerQueryDTO;
import com.clover.salad.customer.query.mapper.CustomerMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerMapper customerMapper;

    @Override
    public CustomerQueryDTO findCustomerById(int id) {
        return customerMapper.findCustomerById(id);
    }

    @Override
    public List<CustomerQueryDTO> findAll() {
        return customerMapper.findAll();
    }

    @Override
    public List<CustomerQueryDTO> findAllActive() {
        return customerMapper.findAllActive();
    }

    @Override
    public Integer findRegisteredCustomerId(String customerName, String customerBirthdate,
            String customerPhone) {
        Integer customerId = customerMapper.findRegisteredCustomerId(customerName,
                customerBirthdate, customerPhone);
        log.debug("조회된 고객 ID: {}", customerId);
        return customerId;
    }
}
