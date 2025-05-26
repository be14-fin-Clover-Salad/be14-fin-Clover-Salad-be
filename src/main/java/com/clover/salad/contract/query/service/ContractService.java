package com.clover.salad.contract.query.service;

import java.util.List;

import com.clover.salad.contract.query.dto.ContractDTO;

public interface ContractService {
	List<ContractDTO> findContractInfo(int employeeId);
}
