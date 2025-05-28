package com.clover.salad.goal.query.service;

import java.util.List;

import com.clover.salad.goal.command.application.dto.EGoalDTO;
import com.clover.salad.goal.command.application.dto.SearchTermDTO;

public interface GoalQueryService {
	List<EGoalDTO> searchEGoalByEIdAndTargetDate(int employeeId, SearchTermDTO searchTerm);
	
	List<EGoalDTO> searchEGoalByDepartmentId(int departmentId, SearchTermDTO searchTerm);
}
