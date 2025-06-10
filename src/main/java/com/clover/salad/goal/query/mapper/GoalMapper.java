package com.clover.salad.goal.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.goal.command.application.dto.DefaultGoalDTO;
import com.clover.salad.goal.command.application.dto.EmployeeSearchTermDTO;
import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.dto.DeptSearchTermDTO;

@Mapper
public interface GoalMapper {
	List<GoalDTO> selectGoalByEmployeeId(EmployeeSearchTermDTO searchTerm);
	
	List<GoalDTO> selectGoalByDepartmentId(DeptSearchTermDTO searchTerm);
	
	DefaultGoalDTO selectDefaultGoalByLevelAndTargetYear(
		@Param("employeeLevel") String employeeLevel,
		@Param("targetYear") int targetYear
	);
	
	List<GoalDTO> selectYearGoalByCurrentGoalTargetDate(
		@Param("employeeId") int employeeId,
		@Param("targetYear") int targetYear
	);
}
