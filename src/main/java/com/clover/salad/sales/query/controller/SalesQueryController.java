package com.clover.salad.sales.query.controller;

import com.clover.salad.sales.query.dto.SalesQueryRequestDTO;
import com.clover.salad.sales.query.dto.SalesQueryResponseDTO;
import com.clover.salad.sales.query.service.SalesQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesQueryController {

	private final SalesQueryService salesQueryService;

	@PostMapping("/search")
	public ResponseEntity<List<SalesQueryResponseDTO>> searchSales(
		@RequestBody SalesQueryRequestDTO query
	) {
		List<SalesQueryResponseDTO> result = salesQueryService.searchSales(query);
		return ResponseEntity.ok(result);
	}
}
