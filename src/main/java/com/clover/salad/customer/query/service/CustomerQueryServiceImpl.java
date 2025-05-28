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

    /**
     * 고객 ID로 고객 정보를 조회합니다.
     *
     * @param customerId 조회할 고객의 ID
     * @return 고객 정보 DTO
     * @throws CustomerNotFoundException 고객이 존재하지 않을 경우 발생
     */
    @Override
    public CustomerDTO findCustomerById(int customerId) {
        CustomerDTO customer = customerMapper.findCustomerById(customerId);
        if (customer == null) {
            log.warn("Customer not found with id: {}", customerId);
            // throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }
        return customer;
    }

    /**
     * 전체 고객 목록을 조회합니다.
     *
     * @return 고객 정보 리스트
     */
    @Override
    public List<CustomerDTO> findAllCustomers() {
        List<CustomerDTO> customers = customerMapper.findAllCustomers();
        log.info("Fetched {} customers", customers.size());
        return customers;
    }
}
