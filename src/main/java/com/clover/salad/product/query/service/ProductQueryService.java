package com.clover.salad.product.query.service;

import java.util.List;

import com.clover.salad.product.command.application.dto.ProductDTO;

public interface ProductQueryService {
	ProductDTO searchProductById(int productId);
	List<ProductDTO> searchProductList();
}
