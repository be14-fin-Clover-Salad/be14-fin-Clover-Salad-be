package com.clover.salad.employee.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clover.salad.employee.query.dto.DepartmentEmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.DepartmentHierarchyDTO;

import com.clover.salad.employee.query.mapper.DepartmentMapper;
import com.clover.salad.employee.query.mapper.EmployeeMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service
public class DepartmentQueryServiceImpl implements DepartmentQueryService {

	private final DepartmentMapper departmentMapper;
	private final EmployeeMapper employeeMapper;

	@Autowired
	public DepartmentQueryServiceImpl(DepartmentMapper departmentMapper,
		EmployeeMapper employeeMapper
	) {
		this.departmentMapper = departmentMapper;
		this.employeeMapper = employeeMapper;
	}

	@Override
	public List<DepartmentHierarchyDTO> getDepartmentHierarchy() {
		return departmentMapper.selectAllDepartmentsWithHierarchy();
	}

	@Override
	@Transactional(readOnly = true)
	public List<DepartmentEmployeeSearchResponseDTO> searchEmployeesByDepartmentWithSub(int departmentId) {
		List<DepartmentHierarchyDTO> subDepartments = departmentMapper.selectSubDepartments(departmentId);
		List<Integer> deptIds = subDepartments.stream().map(DepartmentHierarchyDTO::getId).toList();
		return employeeMapper.searchEmployeesByDeptIds(deptIds);
	}

}
