package com.clover.salad.performance.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.performance.command.domain.aggregate.entity.DepartmentPerformance;

@Repository
public interface DepartmentPerformanceRepository extends JpaRepository<DepartmentPerformance, Integer> {
	DepartmentPerformance findByDepartmentIdAndTargetDate(int deptId, int targetDate);
}
