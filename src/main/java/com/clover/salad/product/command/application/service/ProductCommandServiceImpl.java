package com.clover.salad.product.command.application.service;

import org.springframework.stereotype.Service;

import com.clover.salad.product.command.application.dto.ProductDTO;
import com.clover.salad.product.command.domain.aggregate.entity.Product;
import com.clover.salad.product.command.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandServiceImpl implements ProductCommandService {
	private final ProductRepository productRep;
	
	@Override
	public void registerProduct(ProductDTO productDTO) {
		Product product = new Product();
		productDTOToProduct(productDTO, product);
		productRep.save(product);
	}
	
	@Override
	public void updateProduct(int productId, ProductDTO productDTO) {
		Product product = productRep.findById(productId).orElseThrow();
		productDTOToProduct(productDTO, product);
		productRep.save(product);
	}
	
	@Override
	public String deleteProduct(int productId) {
		Product product = productRep.findById(productId).orElseThrow();
		product.setDeleted(true);
		productRep.save(product);
		return product.getName();
	}
	
	private void productDTOToProduct(ProductDTO productDTO, Product product) {
		product.setCategory(productDTO.getCategory());
		product.setName(productDTO.getName());
		product.setSerialNumber(productDTO.getSerialNumber());
		product.setProductCode(productDTO.getProductCode());
		product.setCompany(productDTO.getCompany());
		product.setOriginCost(productDTO.getOriginCost());
		product.setRentalCost(productDTO.getRentalCost());
		product.setDescription(productDTO.getDescription());
		product.setFileUploadId(productDTO.getFileUploadId());
	}
}
