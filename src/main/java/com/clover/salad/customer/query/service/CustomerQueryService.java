package com.clover.salad.customer.query.service;

import java.util.List;

import com.clover.salad.customer.query.dto.CustomerQueryDTO;

public interface CustomerQueryService {

    List<CustomerQueryDTO> findAll();

    List<CustomerQueryDTO> findCustomersByEmployeeId(int employeeId);

    CustomerQueryDTO findCustomerByEmployeeAndCustomerId(int customerId, int employeeId);

    Integer findRegisteredCustomerId(String name, String birthdate, String phone);
}
