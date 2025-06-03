package com.clover.salad.contract.query.service;

import java.util.List;

import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractDetailInfoResponseDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;

public interface ContractService {
	List<ContractDTO> findContractInfo(int employeeId);

	List<ContractDTO> searchContracts(ContractSearchDTO contractSearchDTO);

	ContractDetailInfoResponseDTO findDetailInfo(int contractId);
}
