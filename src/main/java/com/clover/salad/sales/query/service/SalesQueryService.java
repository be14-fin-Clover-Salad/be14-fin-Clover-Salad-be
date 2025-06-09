package com.clover.salad.sales.query.service;

import com.clover.salad.sales.query.dto.SalesQueryRequestDTO;
import com.clover.salad.sales.query.dto.SalesQueryResponseDTO;

import java.util.List;

public interface SalesQueryService {
	List<SalesQueryResponseDTO> searchSales(SalesQueryRequestDTO query);
}
