package com.clover.salad.customer.query.service;

import java.util.List;

import com.clover.salad.customer.query.dto.CustomerQueryDTO;

public interface CustomerQueryService {

    List<CustomerQueryDTO> findAll();

    CustomerQueryDTO findCustomerById(int customerId);

    List<CustomerQueryDTO> findCustomersByEmployeeId(int employeeId);

    CustomerQueryDTO findCustomerByEmployeeAndCustomerId(int customerId, int employeeId);

    Integer findRegisteredCustomerId(String name, String birthdate, String phone);

    boolean existsContractByCustomer(String name, String birthdate, String phone);

    boolean existsConsultByCustomer(String name, String birthdate, String phone);
}
