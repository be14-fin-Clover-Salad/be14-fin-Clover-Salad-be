package com.clover.salad.product.command.application.service;

import com.clover.salad.product.command.application.dto.ProductDTO;

public interface ProductCommandService {
	void registerProduct(ProductDTO productDTO);
}
