package com.clover.salad.contract.query.mapper;

import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface ContractMapper {
	List<ContractDTO> selectContractInfo(@Param("employeeId") int employeeId);

	List<ContractDTO> searchContracts(ContractSearchDTO contractSearchDTO);

}

