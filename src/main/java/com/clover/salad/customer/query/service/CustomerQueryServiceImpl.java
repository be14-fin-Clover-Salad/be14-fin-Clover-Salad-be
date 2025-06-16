package com.clover.salad.customer.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.CustomerAccessDeniedException;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.customer.query.dto.CustomerQueryDTO;
import com.clover.salad.customer.query.mapper.CustomerMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerMapper customerMapper;
    private final ContractService contractService;

    @Override
    public List<CustomerQueryDTO> findAll() {
        return customerMapper.findAll();
    }

    @Override
    public List<CustomerQueryDTO> findCustomersByEmployeeId(int employeeId) {
        List<Integer> customerIds = contractService.getCustomerIdsByEmployee(employeeId);

        if (customerIds == null) {
            throw new IllegalArgumentException("사원 ID에 해당하는 고객이 없습니다.");
        }

        return customerMapper.findCustomersByIds(customerIds);
    }

    @Override
    public CustomerQueryDTO findCustomerByEmployeeAndCustomerId(int customerId, int employeeId) {
        List<Integer> customerIds = contractService.getCustomerIdsByEmployee(employeeId);

        if (customerIds == null || !customerIds.contains(customerId)) {
            log.warn("사원 ID {}는 고객 ID {}에 대한 접근 권한이 없습니다.", employeeId, customerId);
            throw new CustomerAccessDeniedException("해당 사원이 담당한 고객이 아닙니다.");
        }

        return customerMapper.findCustomerById(customerId);
    }

    @Override
    public Integer findRegisteredCustomerId(String customerName, String customerBirthdate,
            String customerPhone) {
        Integer customerId = customerMapper.findRegisteredCustomerId(customerName,
                customerBirthdate, customerPhone);
        log.debug("조회된 고객 ID: {}", customerId);
        return customerId;
    }
}
