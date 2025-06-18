package com.clover.salad.contract.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractResponseDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;

@Mapper
public interface ContractMapper {
	List<ContractDTO> selectContractInfo(@Param("employeeId") int employeeId);

	List<ContractDTO> searchContracts(@Param("employeeId") int employeeId,
		@Param("search") ContractSearchDTO contractSearchDTO);

	ContractResponseDTO selectDetailContractInfo(int contractId);

	List<Integer> findCustomerIdsByEmployeeId(@Param("employeeId") int employeeId);

	Boolean existsById(int contractId);
}

