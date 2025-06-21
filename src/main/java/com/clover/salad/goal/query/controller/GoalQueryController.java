package com.clover.salad.goal.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.goal.command.application.dto.DefaultGoalDTO;
import com.clover.salad.goal.command.application.dto.EmployeeSearchTermDTO;
import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.dto.DeptSearchTermDTO;
import com.clover.salad.goal.query.service.GoalQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/goal")
@Slf4j
@RequiredArgsConstructor
public class GoalQueryController {
	private final GoalQueryService goalQueryService;
	
	/* 설명. 사원 한 명의 목표 조회
	 *  쿼리스트링 수동 변경에 의한 조회 제한 필요
	 * */
	@GetMapping("/employee")
	public ResponseEntity<List<GoalDTO>> searchGoalByEmployeeId(EmployeeSearchTermDTO searchTerm) {
		return ResponseEntity.ok(goalQueryService.searchGoalByEmployeeId(searchTerm));
	}

	/* 설명. 팀장이 소속된 팀원 모두의 목표 조회
	 *  팀장 권한 설정 필요
	 * */
	@GetMapping("/department")
	public ResponseEntity<List<GoalDTO>> searchGoalByDepartmentId(DeptSearchTermDTO searchTerm) {
		return ResponseEntity.ok(goalQueryService.searchGoalByDepartmentId(searchTerm));
	}

	/* 설명. 실적 목표 수정 버튼 누를 시, 현재 선택된 목표의 연도를 가져와서, 그 해의 목표 조회
	 *  지난 기간에 대한 제한은 프론트에서 구현
	 * */
	@GetMapping("/change/{employeeCode}/{targetYear}")
	public ResponseEntity<List<GoalDTO>> searchYearGoalByCurrentGoal(
		@PathVariable int employeeCode,
		@PathVariable int targetYear
	) {
		return ResponseEntity.ok(goalQueryService.searchYearGoalByCurrentGoal(employeeCode, targetYear));
	}
	
	/* 설명. 기본 실적 목표 조회 */
	@GetMapping("/default/{targetYear}/{level}")
	public ResponseEntity<DefaultGoalDTO> searchDefaultGoal(
		@PathVariable int targetYear,
		@PathVariable String level
	) {
		return ResponseEntity.ok(goalQueryService.searchDefaultGoalByLevelAndTargetYear(level, targetYear));
	}
}
