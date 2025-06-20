package com.clover.salad.employee.query.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.DepartmentEmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.EmployeeDetailDTO;
import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchRequestDTO;
import com.clover.salad.employee.query.dto.EmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;

@Service
public interface EmployeeQueryService {

	List<EmployeeSearchResponseDTO> searchEmployees(EmployeeSearchRequestDTO requestDTO);

	UserDetails loadUserById(int id);

	LoginHeaderInfoDTO getLoginHeaderInfoById(int id);

	EmployeeMypageQueryDTO getMyPageInfoById(int id);

	// boolean checkIsAdmin(String code);
	// boolean checkIsAdminById(int id);

	String findCodeById(int id);

	String findNameById(int id);

	List<DepartmentEmployeeSearchResponseDTO> searchEmployeesByDepartmentWithSub(int departmentId);

	// 사원 상세 조회
	EmployeeDetailDTO getEmployeeDetailById(int id);
}
