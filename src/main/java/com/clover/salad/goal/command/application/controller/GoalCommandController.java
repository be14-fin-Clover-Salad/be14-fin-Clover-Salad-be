package com.clover.salad.goal.command.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
