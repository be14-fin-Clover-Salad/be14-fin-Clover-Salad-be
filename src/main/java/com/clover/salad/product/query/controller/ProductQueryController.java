package com.clover.salad.product.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.product.command.application.dto.ProductDTO;
import com.clover.salad.product.command.application.dto.SearchTermDTO;
import com.clover.salad.product.query.service.ProductQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/product")
@Slf4j
@RequiredArgsConstructor
public class ProductQueryController {
	private final ProductQueryService productQueryService;
	
	/* 설명. 상품 상세 조회 */
	@GetMapping("/detail/{productId}")
	public ResponseEntity<ProductDTO> searchProductById(@PathVariable("productId") int productId) {
		return ResponseEntity.ok(productQueryService.searchProductById(productId));
	}
	
	/* 설명. 상품 목록 조회 */
	@GetMapping("/list")
	public ResponseEntity<List<ProductDTO>> searchProductList(SearchTermDTO searchTerm) {
		log.info("Search term: {}", searchTerm);
		List<ProductDTO> productList = productQueryService.searchProductList(searchTerm);
		log.info("Search result: {}", productList);
		return ResponseEntity.ok(productList);
	}
}
