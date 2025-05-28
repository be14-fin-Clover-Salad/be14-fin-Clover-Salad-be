package com.clover.salad.customer.query.service;

import java.util.List;

import com.clover.salad.customer.command.application.dto.CustomerDTO;
// import com.clover.salad.customer.query.exception.CustomerNotFoundException;

/**
 * 고객 조회 기능을 제공하는 서비스 인터페이스입니다.
 */
public interface CustomerQueryService {

    /**
     * 고객 ID를 기반으로 고객 정보를 조회합니다.
     *
     * @param customerId 고객의 고유 ID
     * @return 해당 ID에 해당하는 CustomerDTO
     * @throws CustomerNotFoundException 고객이 존재하지 않을 경우
     */
    CustomerDTO findCustomerById(int customerId);

    /**
     * 모든 고객 정보를 조회합니다.
     *
     * @return 고객 정보 DTO 리스트
     */
    List<CustomerDTO> findAllCustomers();
}
