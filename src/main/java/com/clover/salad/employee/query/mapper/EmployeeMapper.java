package com.clover.salad.employee.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;

@Mapper
public interface EmployeeMapper {
	List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchEmployeeDTO);

	LoginHeaderInfoDTO findLoginHeaderInfoByCode(@Param("code") String code);
}
