package com.clover.salad.salesDashboard.query.service;

import com.clover.salad.salesDashboard.query.dto.*;
import com.clover.salad.salesDashboard.query.mapper.SalesDashboardQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class SalesDashboardQueryServiceImpl implements SalesDashboardQueryService {
	private final SalesDashboardQueryMapper salesDashboardQueryMapper;

	@Autowired
	public SalesDashboardQueryServiceImpl(SalesDashboardQueryMapper salesDashboardQueryMapper) {
		this.salesDashboardQueryMapper = salesDashboardQueryMapper;
	}

	@Override
	public SalesTotalResponseDTO getTotalSalesByPeriod(SalesTotalRequestDTO request) {
		String period = request.getPeriod().toLowerCase();
		LocalDate today = LocalDate.now();
		LocalDate startDate;
		LocalDate endDate;

		switch (period) {
			case "year" -> {
				startDate = today.withDayOfYear(1);
				endDate = today;
			}
			case "lastyear" -> {
				int lastYear = today.getYear() - 1;
				startDate = LocalDate.of(lastYear, 1, 1);
				endDate = LocalDate.of(lastYear, 12, 31);
			}
			case "quarter" -> {
				LocalDate[] range = getQuarterDateRange(today.getYear(), getQuarter(today.getMonthValue()));
				startDate = range[0];
				endDate = today;
			}
			case "lastquarter" -> {
				int currentQuarter = getQuarter(today.getMonthValue());
				int lastQuarter = currentQuarter == 1 ? 4 : currentQuarter - 1;
				int year = currentQuarter == 1 ? today.getYear() - 1 : today.getYear();
				LocalDate[] range = getQuarterDateRange(year, lastQuarter);
				startDate = range[0];
				endDate = range[1];
			}
			case "month" -> {
				startDate = today.withDayOfMonth(1);
				endDate = today;
			}
			case "lastmonth" -> {
				LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);
				LocalDate lastMonthEnd = firstDayOfThisMonth.minusDays(1);
				startDate = lastMonthEnd.withDayOfMonth(1);
				endDate = lastMonthEnd;
			}
			default -> throw new IllegalArgumentException("유효하지 않은 기간입니다: " + period);
		}

		int total = salesDashboardQueryMapper.selectTotalSalesByDateRange(startDate.toString(), endDate.toString());
		return new SalesTotalResponseDTO(period, startDate, endDate, total);
	}

	@Override
	public List<SalesMonthlyTrendResponseDTO> getMonthlySalesTrend(int year) {
		return salesDashboardQueryMapper.selectMonthlySalesByYear(year);
	}

	@Override
	public List<SalesQuarterlyTrendResponseDTO> getQuarterlySalesTrend(int year) {
		return salesDashboardQueryMapper.selectQuarterlySalesByYear(year);
	}

	@Override
	public List<SalesYearlyTrendResponseDTO> getYearlySalesTrend() {
		int endYear = LocalDate.now().getYear();
		int startYear = endYear - 9;
		return salesDashboardQueryMapper.selectYearlySalesRange(startYear, endYear);
	}

	@Override
	public List<SalesTeamRatioResponseDTO> getSalesTeamRatioByPeriod(String period) {
		LocalDate today = LocalDate.now();
		LocalDate startDate;

		switch (period) {
			case "year" -> startDate = today.withDayOfYear(1);
			case "quarter" -> startDate = getQuarterDateRange(today.getYear(), getQuarter(today.getMonthValue()))[0];
			case "month" -> startDate = today.withDayOfMonth(1);
			default -> throw new IllegalArgumentException("지원하지 않는 기간입니다: " + period);
		}

		return calculateRatio(startDate, today);
	}

	@Override
	public List<SalesTeamRatioResponseDTO> getMonthlyTeamSalesRatio(int year, int month) {
		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
		return calculateRatio(start, end);
	}

	@Override
	public List<SalesTeamRatioResponseDTO> getQuarterlyTeamSalesRatio(int year, int quarter) {
		LocalDate[] range = getQuarterDateRange(year, quarter);
		return calculateRatio(range[0], range[1]);
	}

	@Override
	public List<SalesTeamRatioResponseDTO> getYearlyTeamSalesRatio(int year) {
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end = LocalDate.of(year, 12, 31);
		return calculateRatio(start, end);
	}

	@Override
	public List<SalesTeamAmountResponseDTO> getTeamSalesAmountByMonth(int year, int month) {
		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
		return salesDashboardQueryMapper.selectTeamSalesAmountInRange(start, end);
	}

	@Override
	public List<SalesTeamAmountResponseDTO> getTeamSalesAmountByQuarter(int year, int quarter) {
		LocalDate[] range = getQuarterDateRange(year, quarter);
		return salesDashboardQueryMapper.selectTeamSalesAmountInRange(range[0], range[1]);
	}

	@Override
	public List<SalesTeamAmountResponseDTO> getTeamSalesAmountByYear(int year) {
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end = LocalDate.of(year, 12, 31);
		return salesDashboardQueryMapper.selectTeamSalesAmountInRange(start, end);
	}

	private int getQuarter(int month) {
		return ((month - 1) / 3) + 1;
	}

	private LocalDate[] getQuarterDateRange(int year, int quarter) {
		return switch (quarter) {
			case 1 -> new LocalDate[]{LocalDate.of(year, 1, 1), LocalDate.of(year, 3, 31)};
			case 2 -> new LocalDate[]{LocalDate.of(year, 4, 1), LocalDate.of(year, 6, 30)};
			case 3 -> new LocalDate[]{LocalDate.of(year, 7, 1), LocalDate.of(year, 9, 30)};
			case 4 -> new LocalDate[]{LocalDate.of(year, 10, 1), LocalDate.of(year, 12, 31)};
			default -> throw new IllegalArgumentException("분기는 1~4 사이의 값이어야 합니다.");
		};
	}

	private List<SalesTeamRatioResponseDTO> calculateRatio(LocalDate startDate, LocalDate endDate) {
		List<SalesTeamRatioRawDTO> rawResults =
			salesDashboardQueryMapper.selectTeamSalesInPeriod(startDate, endDate);
		int total = rawResults.stream().mapToInt(SalesTeamRatioRawDTO::getTeamAmount).sum();
		return rawResults.stream().map(dto ->
			new SalesTeamRatioResponseDTO(
				dto.getTeamName(),
				dto.getTeamAmount(),
				total == 0 ? 0.0 : Math.round(dto.getTeamAmount() * 1000.0 / total) / 10.0
			)).toList();
	}
}
