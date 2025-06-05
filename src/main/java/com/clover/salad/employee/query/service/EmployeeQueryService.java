package com.clover.salad.employee.query.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;

@Service
public interface EmployeeQueryService {
	List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchDto);

	UserDetails loadUserByUsername(String subject);

	LoginHeaderInfoDTO getLoginHeaderInfo(String code);

	boolean checkIsAdmin(String code);
}
