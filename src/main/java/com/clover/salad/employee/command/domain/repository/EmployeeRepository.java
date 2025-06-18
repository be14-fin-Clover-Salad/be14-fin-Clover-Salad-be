package com.clover.salad.employee.command.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
	Optional<EmployeeEntity> findByCode(String code);

	Optional<EmployeeEntity> findByEmail(String email);
	
	List<EmployeeEntity> findByDepartmentIdAndIsAdmin(int deptId, boolean admin);
}