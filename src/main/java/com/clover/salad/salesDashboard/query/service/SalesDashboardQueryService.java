package com.clover.salad.salesDashboard.query.service;

import java.util.List;

import com.clover.salad.salesDashboard.query.dto.*;

public interface SalesDashboardQueryService {

	// 전체 매출 조회 (이번 달 / 분기 / 연도 등)
	SalesTotalResponseDTO getTotalSalesByPeriod(SalesTotalRequestDTO request);

	// 매출 추이
	List<SalesMonthlyTrendResponseDTO> getMonthlySalesTrend(int year);
	List<SalesQuarterlyTrendResponseDTO> getQuarterlySalesTrend(int year);
	List<SalesYearlyTrendResponseDTO> getYearlySalesTrend();

	// 이번 달 / 분기 / 연도 팀별 비율
	List<SalesTeamRatioResponseDTO> getSalesTeamRatioByPeriod(String period);

	// 특정 월 / 분기 / 연도 팀별 비율
	List<SalesTeamRatioResponseDTO> getMonthlyTeamSalesRatio(int year, int month);
	List<SalesTeamRatioResponseDTO> getQuarterlyTeamSalesRatio(int year, int quarter);
	List<SalesTeamRatioResponseDTO> getYearlyTeamSalesRatio(int year);

	// 특정 월 / 분기 / 연도 팀별 총매출
	List<SalesTeamAmountResponseDTO> getTeamSalesAmountByMonth(int year, int month);
	List<SalesTeamAmountResponseDTO> getTeamSalesAmountByQuarter(int year, int quarter);
	List<SalesTeamAmountResponseDTO> getTeamSalesAmountByYear(int year);
}
