package com.clover.salad.salesDashboard.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.salesDashboard.query.dto.*;
import com.clover.salad.salesDashboard.query.service.SalesDashboardQueryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/dashboard/sales")
public class SalesDashboardQueryController {
	private final SalesDashboardQueryService salesDashboardQueryService;

	@Autowired
	public SalesDashboardQueryController(SalesDashboardQueryService salesDashboardQueryService) {
		this.salesDashboardQueryService = salesDashboardQueryService;
	}

	/* 설명. 오늘 기준 전체 매출 합산 (이번 달, 분기, 연도 등) */
	@GetMapping("/total")
	public ResponseEntity<SalesTotalResponseDTO> getTotalSales(@RequestParam String period) {
		SalesTotalRequestDTO request = new SalesTotalRequestDTO(period);
		SalesTotalResponseDTO response = salesDashboardQueryService.getTotalSalesByPeriod(request);
		return ResponseEntity.ok(response);
	}

	/* 설명. 월별 매출 추이 */
	@GetMapping("/trend/monthly")
	public ResponseEntity<List<SalesMonthlyTrendResponseDTO>> getMonthlySalesTrend(@RequestParam int year) {
		return ResponseEntity.ok(salesDashboardQueryService.getMonthlySalesTrend(year));
	}

	/* 설명. 분기별 매출 추이 */
	@GetMapping("/trend/quarterly")
	public ResponseEntity<List<SalesQuarterlyTrendResponseDTO>> getQuarterlySalesTrend(@RequestParam int year) {
		return ResponseEntity.ok(salesDashboardQueryService.getQuarterlySalesTrend(year));
	}

	/* 설명. 연도별 매출 추이 */
	@GetMapping("/trend/yearly")
	public ResponseEntity<List<SalesYearlyTrendResponseDTO>> getYearlySalesTrend() {
		return ResponseEntity.ok(salesDashboardQueryService.getYearlySalesTrend());
	}

	/* 설명. 오늘 기준 팀별 비중 (이번 달, 분기, 연도) */
	@GetMapping("/ratio")
	public ResponseEntity<List<SalesTeamRatioResponseDTO>> getSalesTeamRatioByPeriod(@RequestParam String period) {
		return ResponseEntity.ok(salesDashboardQueryService.getSalesTeamRatioByPeriod(period));
	}

	/* 설명. 특정 월의 팀별 매출 비중 */
	@GetMapping("/ratio/monthly/detail")
	public ResponseEntity<List<SalesTeamRatioResponseDTO>> getMonthlyTeamRatio(
		@RequestParam int year,
		@RequestParam int month
	) {
		return ResponseEntity.ok(salesDashboardQueryService.getMonthlyTeamSalesRatio(year, month));
	}

	/* 설명. 특정 분기의 팀별 매출 비중 */
	@GetMapping("/ratio/quarterly/detail")
	public ResponseEntity<List<SalesTeamRatioResponseDTO>> getQuarterlyTeamRatio(
		@RequestParam int year,
		@RequestParam int quarter
	) {
		return ResponseEntity.ok(salesDashboardQueryService.getQuarterlyTeamSalesRatio(year, quarter));
	}

	/* 설명. 특정 연도의 팀별 매출 비중 */
	@GetMapping("/ratio/yearly/detail")
	public ResponseEntity<List<SalesTeamRatioResponseDTO>> getYearlyTeamRatio(
		@RequestParam int year
	) {
		return ResponseEntity.ok(salesDashboardQueryService.getYearlyTeamSalesRatio(year));
	}

	/* 설명. 특정 월의 팀별 총매출 */
	@GetMapping("/amount/monthly/detail")
	public ResponseEntity<List<SalesTeamAmountResponseDTO>> getTeamSalesAmountByMonth(
		@RequestParam int year,
		@RequestParam int month
	) {
		return ResponseEntity.ok(salesDashboardQueryService.getTeamSalesAmountByMonth(year, month));
	}

	/* 설명. 특정 분기의 팀별 총매출 */
	@GetMapping("/amount/quarterly/detail")
	public ResponseEntity<List<SalesTeamAmountResponseDTO>> getTeamSalesAmountByQuarter(
		@RequestParam int year,
		@RequestParam int quarter
	) {
		return ResponseEntity.ok(salesDashboardQueryService.getTeamSalesAmountByQuarter(year, quarter));
	}

	/* 설명. 특정 연도의 팀별 총매출 */
	@GetMapping("/amount/yearly/detail")
	public ResponseEntity<List<SalesTeamAmountResponseDTO>> getTeamSalesAmountByYear(
		@RequestParam int year
	) {
		return ResponseEntity.ok(salesDashboardQueryService.getTeamSalesAmountByYear(year));
	}
}
