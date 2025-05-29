package com.clover.salad.employee.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.dto.SearchEmployeeDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;

@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService {
	private final EmployeeMapper employeeMapper;

	@Autowired
	public EmployeeQueryServiceImpl(EmployeeMapper employeeMapper) {
		this.employeeMapper = employeeMapper;
	}

	@Override
	public List<EmployeeQueryDTO> searchEmployees(SearchEmployeeDTO searchEmployeeDTO) {
		return employeeMapper.searchEmployees(searchEmployeeDTO);
	}
}
