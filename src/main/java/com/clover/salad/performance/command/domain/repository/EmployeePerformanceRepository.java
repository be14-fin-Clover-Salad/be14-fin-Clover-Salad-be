package com.clover.salad.performance.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.performance.command.domain.aggregate.entity.EmployeePerformance;

@Repository
public interface EmployeePerformanceRepository extends JpaRepository<EmployeePerformance, Integer> {
	EmployeePerformance findByEmployeeIdAndTargetDate(int employeeId, int targetDate);
}
