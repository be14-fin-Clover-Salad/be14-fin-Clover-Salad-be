package com.clover.salad.goal.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.goal.command.application.dto.EGoalDTO;
import com.clover.salad.goal.command.application.dto.SearchTermDTO;

@Mapper
public interface GoalMapper {
	List<EGoalDTO> selectEmployeeGoalByEIdAndTargetDate(SearchTermDTO searchTerm);
	
	List<EGoalDTO> selectEmployeeGoalByDepartmentId(SearchTermDTO searchTerm);
}
