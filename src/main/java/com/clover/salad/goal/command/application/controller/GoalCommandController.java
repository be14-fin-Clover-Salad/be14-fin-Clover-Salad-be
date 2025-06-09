package com.clover.salad.goal.command.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.goal.command.application.dto.GoalDTO;
import com.clover.salad.goal.command.application.service.GoalCommandService;
import com.clover.salad.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/goal")
@Slf4j
@RequiredArgsConstructor
public class GoalCommandController {
	private final GoalCommandService goalCommandService;
	private final JwtUtil jwtUtil;
	
	/* 설명. 실적 목표 등록 */
	@PostMapping("/register")
	public ResponseEntity<String> registerGoal(
		@RequestHeader("Authorization") String token,
		@RequestBody List<GoalDTO> goalList
	) {
		String pureToken = token.replace("Bearer ", "");
		String code = jwtUtil.getUsername(pureToken);
		
		log.info("Registering goal");
		goalCommandService.registerGoal(goalList, code);
		
		return ResponseEntity.ok("Goal Registered");
	}
	
	/* 설명. 실적 목표 수정 */
	@PutMapping("/change")
	public ResponseEntity<String> changeGoal(
		@RequestHeader("Authorization") String token,
		@RequestBody List<GoalDTO> goalList
	) {
		
		String pureToken = token.replace("Bearer ", "");
		String code = jwtUtil.getUsername(pureToken);
		
		log.info("Changing goal");
		goalCommandService.changeGoal(goalList, code);
		
		return ResponseEntity.ok("Goal Changed");
	}
	
	/* 설명. 실적 목표 삭제
	 *  관리자가 퇴사 등으로 인해 달성할 수 없어진 목표를 삭제하는 기능
	 *  목표를 조회 후 선택해서 삭제
	 * */
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteGoal(
		@RequestHeader("Authorization") String token,
		@RequestBody List<GoalDTO> goalList
	) {
		String pureToken = token.replace("Bearer ", "");
		String code = jwtUtil.getUsername(pureToken);
		
		log.info("Deleting goal");
		goalCommandService.deleteGoal(goalList, code);
		
		return ResponseEntity.ok("Goals Deleted");
	}
}
