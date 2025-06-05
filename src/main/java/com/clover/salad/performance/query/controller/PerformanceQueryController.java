package com.clover.salad.performance.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.application.dto.SearchTermDTO;
import com.clover.salad.performance.query.service.PerformanceQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/performance")
@Slf4j
@RequiredArgsConstructor
public class PerformanceQueryController {
	private final PerformanceQueryService performanceQueryService;
	
	/* 설명. 사번과 기간에 따른 실적 목록 조회 */
	@GetMapping("/{employeeCode}")
	public ResponseEntity<List<EmployeePerformanceDTO>> searchEmployeePerformanceByEmployeeCode(
		@PathVariable String employeeCode,
		SearchTermDTO searchTerm
	) {
		log.info("employeeCode: {}", employeeCode);
		log.info("searchTerm: {}", searchTerm);
		return ResponseEntity.ok(
			performanceQueryService.searchEmployeePerformanceByEmployeeCode(
				employeeCode,
				searchTerm
			)
		);
	}
}
