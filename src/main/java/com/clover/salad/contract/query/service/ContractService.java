package com.clover.salad.contract.query.service;

import java.util.List;

import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractResponseDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;

public interface ContractService {
	List<ContractDTO> findContractInfo(int employeeId);

	List<ContractDTO> searchContracts(int employeeId, ContractSearchDTO contractSearchDTO);

	ContractResponseDTO findDetailInfo(int contractId);

	List<Integer> getCustomerIdsByEmployee(int employeeId);

	Boolean contractValidationById(int contractId);
}
