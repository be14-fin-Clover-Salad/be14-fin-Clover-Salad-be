package com.clover.salad.product.command.application.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.product.command.application.dto.ProductDTO;
import com.clover.salad.product.command.application.dto.ProductImageDTO;

public interface ProductCommandService {
	void registerProduct(ProductDTO productDTO);
	
	void updateProduct(int productId, ProductDTO productDTO);
	
	String deleteProduct(int productId);
	
	ProductImageDTO uploadProductImage(MultipartFile file) throws IOException;
}
