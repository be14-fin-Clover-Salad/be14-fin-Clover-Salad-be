package com.clover.salad.performance.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.performance.command.application.service.PerformanceCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/performance")
@Slf4j
@RequiredArgsConstructor
public class PerformanceCommandController {
	private final PerformanceCommandService performanceCommandService;

	/* 설명. 기본적으로 자동 갱신되지만 수동으로도 갱신할 수 있도록 함 */
	/* 설명. 개인 실적 추가/갱신 */
	@PutMapping("/refresh/employee/{employeeCode}/{targetDate}")
	public ResponseEntity<String> refreshEmployeePerformance(
		@PathVariable("employeeCode") String employeeCode,
		@PathVariable("targetDate") int targetDate /* yyyyMM */
	) {
		performanceCommandService.refreshEmployeePerformance(employeeCode, targetDate);
		return ResponseEntity.ok("개인 실적 항목 갱신 완료");
	}

	/* 설명. 팀 실적 추가/갱신 */
	@PutMapping("/refresh/department/{deptName}/{targetDate}")
	public ResponseEntity<String> refreshDepartmentPerformance(
		@PathVariable("deptName") String deptName,
		@PathVariable("targetDate") int targetDate
	) {
		performanceCommandService.refreshDepartmentPerformance(deptName, targetDate);
		return ResponseEntity.ok("팀 실적 항목 갱신 완료");
	}
}
