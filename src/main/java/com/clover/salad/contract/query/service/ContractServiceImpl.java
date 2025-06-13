package com.clover.salad.contract.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractResponseDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;
import com.clover.salad.contract.query.mapper.ContractMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContractServiceImpl implements ContractService {

	private final ContractMapper contractMapper;

	@Autowired
	public ContractServiceImpl(ContractMapper contractMapper) {
		this.contractMapper = contractMapper;
	}

	@Override
	public List<ContractDTO> findContractInfo(int employeeId) {
		return contractMapper.selectContractInfo(employeeId);
	}

	@Override
	public List<ContractDTO> searchContracts(int employeeId, ContractSearchDTO contractSearchDTO) {
		return contractMapper.searchContracts(employeeId, contractSearchDTO);
	}

	@Override
	public ContractResponseDTO findDetailInfo(int contractId) {
		return contractMapper.selectDetailContractInfo(contractId);
	}

	@Override
	public List<Integer> getCustomerIdsByEmployee(int employeeId) {
		return contractMapper.findCustomerIdsByEmployeeId(employeeId);
	}

	@Override
	public Boolean contractValidationById(int contractId) {
		return contractMapper.existsById(contractId);
	}
}
