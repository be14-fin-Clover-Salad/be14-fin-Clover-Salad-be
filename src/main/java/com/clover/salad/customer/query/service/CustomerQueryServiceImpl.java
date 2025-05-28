package com.clover.salad.customer.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.customer.command.application.dto.CustomerDTO;
import com.clover.salad.customer.query.mapper.CustomerMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerQueryServiceImpl implements CustomerQueryService {
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO searchCustomerById(int customerId) {
        return customerMapper.selectCustomerById(customerId);
    }

    @Override
    public List<CustomerDTO> searchAllCustomers() {
        return customerMapper.selectAllCustomers();
    }
}
