
package com.clover.salad.salesDashboard.query.mapper;

import com.clover.salad.salesDashboard.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SalesDashboardQueryMapper {
	int selectTotalSalesByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

	List<SalesMonthlyTrendResponseDTO> selectMonthlySalesByYear(@Param("year") int year);

	List<SalesQuarterlyTrendResponseDTO> selectQuarterlySalesByYear(@Param("year") int year);

	List<SalesYearlyTrendResponseDTO> selectYearlySalesRange(@Param("startYear") int startYear, @Param("endYear") int endYear);

	List<SalesTeamRatioRawDTO> selectTeamSalesInPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	List<SalesTeamAmountResponseDTO> selectTeamSalesAmountInRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
