package com.clover.salad.employee.command.domain.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;

@Mapper
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
	Optional<EmployeeEntity> findByCode(String code);

	Optional<EmployeeEntity> findByEmail(String email);
}