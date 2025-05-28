package com.clover.salad.goal.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.goal.command.application.dto.EGoalDTO;
import com.clover.salad.goal.command.application.dto.SearchTermDTO;
import com.clover.salad.goal.query.mapper.GoalMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalQueryServiceImpl implements GoalQueryService {
	private final GoalMapper goalMapper;
	
	@Override
	public List<EGoalDTO> searchEGoalByEIdAndTargetDate(int employeeId, SearchTermDTO searchTerm) {
		searchTerm.setId(employeeId);
		return goalMapper.selectEmployeeGoalByEIdAndTargetDate(searchTerm);
	}
	
	@Override
	public List<EGoalDTO> searchEGoalByDepartmentId(int departmentId, SearchTermDTO searchTerm) {
		searchTerm.setId(departmentId);
		return goalMapper.selectEmployeeGoalByDepartmentId(searchTerm);
	}
}
