package com.clover.salad.product.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clover.salad.product.command.application.dto.ProductDTO;
import com.clover.salad.product.query.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueryServiceImpl implements ProductQueryService {
	private final ProductMapper productMapper;
	
	@Override
	public ProductDTO searchProductById(int productId) {
		return productMapper.selectProductById(productId);
	}
	
	@Override
	public List<ProductDTO> searchProductList() {
		return productMapper.selectProductList();
	}
}
