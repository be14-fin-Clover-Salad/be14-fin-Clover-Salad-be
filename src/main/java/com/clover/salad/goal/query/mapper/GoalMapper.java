package com.clover.salad.goal.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.goal.command.application.dto.EGoalDTO;

@Mapper
public interface GoalMapper {
	List<EGoalDTO> selectEmployeeGoalByEIdAndTargetDate(
		@Param("employeeId") int employeeId,
		@Param("startDate") int startDate,
		@Param("endDate") int endDate
	);
}
