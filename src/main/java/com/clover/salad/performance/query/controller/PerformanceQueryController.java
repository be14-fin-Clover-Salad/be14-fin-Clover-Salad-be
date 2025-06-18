package com.clover.salad.performance.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.performance.command.application.dto.DepartmentPerformanceDTO;
import com.clover.salad.performance.command.application.dto.EmployeePerformanceDTO;
import com.clover.salad.performance.command.application.dto.ProductPerformancePerMonthDTO;
import com.clover.salad.performance.command.application.dto.SearchTermDTO;
import com.clover.salad.performance.query.service.PerformanceQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/performance")
@Slf4j
@RequiredArgsConstructor
public class PerformanceQueryController {
	private final PerformanceQueryService performanceQueryService;
	
	/* 설명. 사번과 기간에 따른 실적 목록 조회 */
	@GetMapping("/employee/{employeeCode}")
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
	
	/* 설명. 팀명과 기간에 따른 실적 목록 조회 */
	@GetMapping("/department/{deptName}")
	public ResponseEntity<List<DepartmentPerformanceDTO>> searchDepartmentPerformanceByDepartmentName(
		@PathVariable String deptName,
		SearchTermDTO searchTerm
	) {
		return ResponseEntity.ok(
			performanceQueryService.searchDepartmentPerformanceByDepartmentName(
				deptName,
				searchTerm
			)
		);
	}
	
	/* 설명. 지정된 기간에 대한 상품별 판매 수량 조회 */
	@GetMapping("/product/{productCode}")
	public ResponseEntity<List<ProductPerformancePerMonthDTO>> searchProductPerformanceByProductCode(
		@PathVariable String productCode,
		SearchTermDTO searchTerm
	) {
		return ResponseEntity.ok(
			performanceQueryService.searchProductPerformanceByProductCode(
				productCode,
				searchTerm
			)
		);
	}
}
