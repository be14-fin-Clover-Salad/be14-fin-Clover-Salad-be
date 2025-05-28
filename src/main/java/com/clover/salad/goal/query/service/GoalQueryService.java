package com.clover.salad.goal.query.service;

import java.util.List;

import com.clover.salad.goal.command.application.dto.EGoalDTO;
import com.clover.salad.goal.command.application.dto.TargetDateDTO;

public interface GoalQueryService {
	List<EGoalDTO> searchEGoalByEIdAndTargetDate(int employeeId, TargetDateDTO input);
}
