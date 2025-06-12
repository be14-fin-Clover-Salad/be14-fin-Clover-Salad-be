package com.clover.salad.employee.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.employee.command.domain.aggregate.entity.DepartmentEntity;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {
	DepartmentEntity findByName(String name);
}