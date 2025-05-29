package com.clover.salad.goal.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.dto.SearchTermDTO;

@Mapper
public interface GoalMapper {
	List<GoalDTO> selectGoalByEmployeeId(SearchTermDTO searchTerm);
	
	List<GoalDTO> selectGoalByDepartmentId(SearchTermDTO searchTerm);
}
