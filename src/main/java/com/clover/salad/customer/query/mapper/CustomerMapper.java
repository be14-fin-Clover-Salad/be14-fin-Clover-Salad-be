package com.clover.salad.customer.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.customer.command.application.dto.CustomerDTO;

@Mapper
public interface CustomerMapper {
    CustomerDTO selectCustomerById(int customerId);

    List<CustomerDTO> selectAllCustomers();
}
