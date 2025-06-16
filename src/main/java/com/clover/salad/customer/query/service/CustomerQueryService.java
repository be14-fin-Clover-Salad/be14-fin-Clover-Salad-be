package com.clover.salad.customer.query.service;

import java.util.List;

import com.clover.salad.customer.query.dto.CustomerQueryDTO;

public interface CustomerQueryService {

    List<CustomerQueryDTO> findAll();

    CustomerQueryDTO findCustomerById(int id);

    List<CustomerQueryDTO> findCustomersByEmployeeId(int employeeId);

    Integer findRegisteredCustomerId(String name, String birthdate, String phone);
}
