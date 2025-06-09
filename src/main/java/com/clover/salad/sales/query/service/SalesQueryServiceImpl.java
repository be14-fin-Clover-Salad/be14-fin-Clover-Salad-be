package com.clover.salad.sales.query.service;

import com.clover.salad.sales.query.dto.SalesQueryRequestDTO;
import com.clover.salad.sales.query.dto.SalesQueryResponseDTO;
import com.clover.salad.sales.query.mapper.SalesQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesQueryServiceImpl implements SalesQueryService {

	private final SalesQueryMapper mapper;

	@Override
	public List<SalesQueryResponseDTO> searchSales(SalesQueryRequestDTO query) {
		return mapper.selectSalesByCondition(query);
	}
}
