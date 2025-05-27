package com.clover.salad.customer.query.service;

import java.util.List;

import com.clover.salad.customer.command.application.dto.CustomerDTO;

public interface CustomerQueryService {
    CustomerDTO searchCustomerById(int customerId);

    List<CustomerDTO> searchAllCustomers();
}
