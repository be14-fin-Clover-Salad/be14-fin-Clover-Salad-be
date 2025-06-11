package com.clover.salad.employee.query.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.EmployeeMypageQueryDTO;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;

@Service
public interface EmployeeQueryService {

	List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchDTO);

	UserDetails loadUserById(int id);

	LoginHeaderInfoDTO getLoginHeaderInfoById(int id);

	EmployeeMypageQueryDTO getMyPageInfoById(int id);

	// boolean checkIsAdmin(String code);
	boolean checkIsAdminById(int id);

	String findCodeById(int id);

	String findNameById(int id);
}