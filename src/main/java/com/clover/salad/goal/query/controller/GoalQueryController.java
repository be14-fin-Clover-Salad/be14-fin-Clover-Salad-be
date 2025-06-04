package com.clover.salad.goal.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.dto.SearchTermDTO;
import com.clover.salad.goal.query.service.GoalQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/goal")
@Slf4j
@RequiredArgsConstructor
public class GoalQueryController {
	private final GoalQueryService goalQueryService;
	
	/* 설명. 사원 한 명의 목표 조회
	 *  쿼리스트링 수동 변경에 의한 조회 제한 필요
	 * */
	@GetMapping("/employee")
	public ResponseEntity<List<GoalDTO>> searchGoalByEmployeeId(SearchTermDTO searchTerm) {
		return ResponseEntity.ok(goalQueryService.searchGoalByEmployeeId(searchTerm));
	}
	
	/* 설명. 팀장이 소속된 팀원 모두의 목표 조회
	 *  팀장 권한 설정 필요
	 * */
	@GetMapping("/department")
	public ResponseEntity<List<GoalDTO>> searchGoalByDepartmentId(SearchTermDTO searchTerm) {
		return ResponseEntity.ok(goalQueryService.searchGoalByDepartmentId(searchTerm));
	}
	
	/* 설명. 실적 목표 수정 버튼 누를 시, 현재 선택 목표가 포함된 해의 목표 조회
	 *  지난 기간에 대한 제한은 프론트에서 구현
	 * */
	@GetMapping("/change/{targetYear}")
	public ResponseEntity<List<GoalDTO>> searchYearGoalByCurrentGoal(@PathVariable int targetYear) {
		// TODO. jwt 토큰 파싱
		int employeeId = 1;
		
		return ResponseEntity.ok(goalQueryService.searchYearGoalByCurrentGoal(employeeId, targetYear));
	}
}
