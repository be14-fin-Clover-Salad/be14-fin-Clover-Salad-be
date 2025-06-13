package com.clover.salad.employee.query.service;

import java.util.List;

import com.clover.salad.employee.query.dto.DepartmentEmployeeSearchResponseDTO;
import com.clover.salad.employee.query.dto.DepartmentHierarchyDTO;

public interface DepartmentQueryService {
	List<DepartmentHierarchyDTO> getDepartmentHierarchy();

	List<DepartmentEmployeeSearchResponseDTO> searchEmployeesByDepartmentWithSub(int departmentId);
}
