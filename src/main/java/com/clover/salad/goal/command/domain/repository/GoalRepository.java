package com.clover.salad.goal.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.goal.command.domain.aggregate.entity.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
	Goal findByEmployeeIdAndTargetDate(int employeeId, int targetDate);
}
