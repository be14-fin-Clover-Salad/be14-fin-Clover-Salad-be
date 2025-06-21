package com.clover.salad.goal.query.service;

import java.util.List;

import com.clover.salad.goal.command.application.dto.DefaultGoalDTO;
import com.clover.salad.goal.command.application.dto.EmployeeSearchTermDTO;
import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.dto.DeptSearchTermDTO;

public interface GoalQueryService {
	List<GoalDTO> searchGoalByEmployeeId(EmployeeSearchTermDTO searchTerm);
	
	List<GoalDTO> searchGoalByDepartmentId(DeptSearchTermDTO searchTerm);
	
	DefaultGoalDTO searchDefaultGoalByLevelAndTargetYear(String employeeLevel, int targetYear);
	
	List<GoalDTO> searchYearGoalByCurrentGoal(int employeeCode, int targetYear);
}
