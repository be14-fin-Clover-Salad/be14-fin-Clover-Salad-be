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
		productRep.save(productDTOToProduct(productDTO));
	}
	
	private Product productDTOToProduct(ProductDTO productDTO) {
		Product product = new Product();
		product.setCategory(productDTO.getCategory());
		product.setName(productDTO.getName());
		product.setSerialNumber(productDTO.getSerialNumber());
		product.setProductCode(productDTO.getProductCode());
		product.setCompany(productDTO.getCompany());
		product.setOriginCost(productDTO.getOriginCost());
		product.setRentalCost(productDTO.getRentalCost());
		product.setDescription(productDTO.getDescription());
		product.setFileUploadId(productDTO.getFileUploadId());
		return product;
	}
}
