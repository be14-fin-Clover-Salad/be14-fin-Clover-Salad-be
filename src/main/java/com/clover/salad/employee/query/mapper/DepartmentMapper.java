package com.clover.salad.employee.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.employee.query.dto.DepartmentHierarchyDTO;

@Mapper
public interface DepartmentMapper {
	List<DepartmentHierarchyDTO> selectAllDepartmentsWithHierarchy();

	List<DepartmentHierarchyDTO> selectSubDepartments(@Param("deptId") int deptId);

}
