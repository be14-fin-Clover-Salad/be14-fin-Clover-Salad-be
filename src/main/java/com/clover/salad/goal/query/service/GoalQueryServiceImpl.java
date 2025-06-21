package com.clover.salad.goal.query.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.query.dto.EmployeeSearchRequestDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.goal.command.application.dto.DefaultGoalDTO;
import com.clover.salad.goal.command.application.dto.EmployeeSearchTermDTO;
import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.dto.DeptSearchTermDTO;
import com.clover.salad.goal.query.mapper.GoalMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalQueryServiceImpl implements GoalQueryService {
	private final GoalMapper goalMapper;
	private final UserDetailsService userDetailsService;
	private final EmployeeQueryService employeeQueryService;
	
	@Override
	public List<GoalDTO> searchGoalByEmployeeId(EmployeeSearchTermDTO searchTerm) {
		return goalMapper.selectGoalByEmployeeId(searchTerm);
	}
	
	@Override
	public List<GoalDTO> searchGoalByDepartmentId(DeptSearchTermDTO searchTerm) {
		return goalMapper.selectGoalByDepartmentId(searchTerm);
	}
	
	@Override
	public DefaultGoalDTO searchDefaultGoalByLevelAndTargetYear(String employeeLevel, int targetYear) {
		return goalMapper.selectDefaultGoalByLevelAndTargetYear(employeeLevel, targetYear);
	}
	
	@Override
	public List<GoalDTO> searchYearGoalByCurrentGoal(int employeeCode, int targetYear) {
		EmployeeSearchRequestDTO searchTerm = new EmployeeSearchRequestDTO();
		searchTerm.setCode(String.valueOf(employeeCode));
		int employeeId = employeeQueryService.searchEmployees(searchTerm).get(0).getId();
		return goalMapper.selectYearGoalByCurrentGoalTargetDate(employeeId, targetYear);
	}
}
