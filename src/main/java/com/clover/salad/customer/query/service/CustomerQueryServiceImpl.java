package com.clover.salad.customer.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.CustomersException;
import com.clover.salad.common.util.AuthUtil;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.customer.query.dto.CustomerQueryDTO;
import com.clover.salad.customer.query.mapper.CustomerMapper;
import com.clover.salad.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final JwtUtil jwtUtil;
    private final CustomerMapper customerMapper;
    private final ContractService contractService;

    @Override
    public List<CustomerQueryDTO> findAll() {
        AuthUtil.assertAdmin();
        return customerMapper.findAll();
    }

    @Override
    public CustomerQueryDTO findCustomerById(int customerId) {
        AuthUtil.assertAdmin();
        return customerMapper.findCustomerById(customerId);
    }

    @Override
    public List<CustomerQueryDTO> findCustomersByEmployeeId(int employeeId) {
        String token = AuthUtil.resolveToken();
        int loginEmployeeId = jwtUtil.getEmployeeId(token);

        if (AuthUtil.isMember() && loginEmployeeId != employeeId) {
            throw new CustomersException.CustomerAccessDeniedException(
                    "사원은 본인이 담당하는 고객 정보만 조회할 수 있습니다.");
        }

        List<Integer> customerIds = contractService.getCustomerIdsByEmployee(employeeId);
        if (customerIds == null || customerIds.isEmpty()) {
            throw new CustomersException.CustomerNotFoundException("해당 사원이 담당하는 고객이 존재하지 않습니다.");
        }

        return customerMapper.findCustomersByIds(customerIds);
    }

    @Override
    public CustomerQueryDTO findCustomerByEmployeeAndCustomerId(int customerId, int employeeId) {
        String token = AuthUtil.resolveToken();
        int loginEmployeeId = jwtUtil.getEmployeeId(token);

        // 고객 존재 여부 확인
        CustomerQueryDTO customer = customerMapper.findCustomerById(customerId);
        if (customer == null) {
            throw new CustomersException.CustomerNotFoundException("해당 고객을 조회할 수 없습니다.");
        }

        // 로그인한 사원 본인만 접근 가능 (ROLE_MEMBER인 경우)
        if (AuthUtil.isMember() && loginEmployeeId != employeeId) {
            throw new CustomersException.CustomerAccessDeniedException("해당 고객은 요청한 사원의 담당이 아닙니다.");
        }

        // 고객이 해당 사원의 고객인지 확인
        List<Integer> customerIds = contractService.getCustomerIdsByEmployee(employeeId);
        if (customerIds == null || !customerIds.contains(customerId)) {
            log.warn("사원 ID {}는 고객 ID {}에 대한 접근 권한이 없습니다.", employeeId, customerId);
            throw new CustomersException.CustomerAccessDeniedException("해당 고객은 요청한 사원의 담당이 아닙니다.");
        }

        return customer;
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
