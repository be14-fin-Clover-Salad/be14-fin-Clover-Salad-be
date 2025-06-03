package com.clover.salad.customer.query.service;

import java.util.List;

import com.clover.salad.customer.query.dto.CustomerQueryDTO;

public interface CustomerQueryService {
    CustomerQueryDTO findCustomerById(int id);

    List<CustomerQueryDTO> findAll();

    List<CustomerQueryDTO> findAllActive();
}
