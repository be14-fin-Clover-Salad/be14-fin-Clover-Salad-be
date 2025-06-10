package com.clover.salad.goal.command.application.service;

import java.util.List;

import com.clover.salad.goal.command.application.dto.GoalDTO;

public interface GoalCommandService {
	void registerGoal(List<GoalDTO> goalList, int employeeId);
	
	void changeGoal(List<GoalDTO> goalList, int employeeId);
	
	void deleteGoal(List<GoalDTO> goalList);
}
