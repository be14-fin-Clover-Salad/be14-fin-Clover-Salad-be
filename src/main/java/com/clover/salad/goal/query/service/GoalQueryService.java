package com.clover.salad.goal.query.service;

import java.util.List;

import com.clover.salad.goal.command.application.dto.DefaultGoalDTO;
import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.dto.SearchTermDTO;

public interface GoalQueryService {
	List<GoalDTO> searchGoalByEmployeeId(SearchTermDTO searchTerm);
	
	List<GoalDTO> searchGoalByDepartmentId(SearchTermDTO searchTerm);
	
	DefaultGoalDTO searchDefaultGoalByLevelAndTargetYear(String employeeLevel, int targetYear);
	
	List<GoalDTO> searchYearGoalByCurrentGoal(int employeeId, int targetYear);
}
