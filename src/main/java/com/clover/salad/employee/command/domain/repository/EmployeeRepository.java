package com.clover.salad.employee.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
	EmployeeEntity findByCode(String code);
}
