package com.clover.salad.goal.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.goal.command.application.dto.EGoalDTO;
import com.clover.salad.goal.command.application.dto.TargetDateDTO;
import com.clover.salad.goal.query.service.GoalQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/goal")
@Slf4j
@RequiredArgsConstructor
public class GoalQueryController {
	private final GoalQueryService goalQueryService;
	
	/* 설명. 사원 한 명의 목표 조회 (Employee -> E로 축약) */
	@GetMapping("/{employeeId}")
	public ResponseEntity<List<EGoalDTO>> searchEGoalByEIdAndTargetDate(
		@PathVariable("employeeId") int employeeId,
		@RequestBody TargetDateDTO targetDate
	) {
		return ResponseEntity.ok(
			goalQueryService.searchEGoalByEIdAndTargetDate(employeeId, targetDate)
		);
	}
	
	/* 설명. 팀장이 소속된 팀원 모두의 목표 조회
	*   팀장 권한 설정 필요
	* */
	@GetMapping("/department/{departmentId}")
	public ResponseEntity<List<EGoalDTO>> searchEGoalByDepartmentId(
		@PathVariable("departmentId") int departmentId,
		@RequestBody TargetDateDTO targetDate
	) {
		return ResponseEntity.ok(goalQueryService.searchEGoalByDepartmentId(departmentId, targetDate));
	}
}
