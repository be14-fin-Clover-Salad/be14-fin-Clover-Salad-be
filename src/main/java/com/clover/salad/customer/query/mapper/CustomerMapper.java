package com.clover.salad.customer.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.customer.command.application.dto.CustomerDTO;

@Mapper
public interface CustomerMapper {
    List<CustomerDTO> findAllCustomers();

    CustomerDTO findCustomerById(int customerId);
}
