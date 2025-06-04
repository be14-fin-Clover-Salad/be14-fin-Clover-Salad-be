package com.clover.salad.goal.command.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.service.GoalCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/goal")
@Slf4j
@RequiredArgsConstructor
public class GoalCommandController {
	private final GoalCommandService goalCommandService;
	
	/* 설명. 실적 목표 등록 */
	@PostMapping("/register")
	public ResponseEntity<String> registerGoal(@RequestBody List<GoalDTO> goalList) {
		
		/* TODO. 설명. 임시 사원 id(토큰에서 파싱) */
		int tempEmployeeId = 1;
		
		try {
			log.info("Registering goal");
			goalCommandService.registerGoal(goalList, tempEmployeeId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Goal Registered");
	}
	
	@PutMapping("/change")
	public ResponseEntity<String> changeGoal(@RequestBody List<GoalDTO> goalList) {
		
		/* TODO. 설명. 임시 사원 id(토큰에서 파싱) */
		int tempEmployeeId = 1;
		
		try {
			log.info("Changing goal");
			goalCommandService.changeGoal(goalList, tempEmployeeId);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Goal Changed");
	}
	
	/* 설명. 관리자가 퇴사 등으로 인해 달성할 수 없어진 목표를 삭제하는 기능
	 *  목표를 조회 후 선택해서 삭제
	 * */
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteGoal(@RequestBody List<GoalDTO> goalList) {
		try {
			log.info("Deleting goal");
			goalCommandService.deleteGoal(goalList);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Goals Deleted");
	}
}
