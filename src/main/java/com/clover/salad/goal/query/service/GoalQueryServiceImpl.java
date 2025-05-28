package com.clover.salad.goal.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.goal.command.application.dto.EGoalDTO;
import com.clover.salad.goal.command.application.dto.TargetDateDTO;
import com.clover.salad.goal.query.mapper.GoalMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalQueryServiceImpl implements GoalQueryService {
	private final GoalMapper goalMapper;
	
	@Override
	public List<EGoalDTO> searchEGoalByEIdAndTargetDate(int employeeId, TargetDateDTO targetDate) {
		int startDate;
		int endDate;
		if (targetDate.getStartDate() == null) {
			startDate = 0;
		} else {
			startDate = targetDate.getStartDate();
		}
		if (targetDate.getEndDate() == null) {
			endDate = 999999;
		} else {
			endDate = targetDate.getEndDate();
		}
		return goalMapper.selectEmployeeGoalByEIdAndTargetDate(employeeId, startDate, endDate);
	}
}
