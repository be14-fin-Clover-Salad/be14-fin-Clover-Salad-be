package com.clover.salad.contract.query.mapper;

import com.clover.salad.contract.query.dto.ContractDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {
	List<ContractDTO> selectContractInfo(@Param("employeeId") int employeeId);
}