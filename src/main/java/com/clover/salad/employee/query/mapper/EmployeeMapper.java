package com.clover.salad.employee.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;

@Mapper
public interface EmployeeMapper {
	List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchEmployeeDTO);

	LoginHeaderInfoDTO findLoginHeaderInfoByCode(@Param("code") String code);

	Boolean selectIsAdminByCode(@Param("code") String code);

	Integer selectIdByCodeAndEmail(@Param("code") String code, @Param("email") String email);

	EmployeeMypageQueryDTO selectMyPageInfoByCode(@Param("code") String code);
}
