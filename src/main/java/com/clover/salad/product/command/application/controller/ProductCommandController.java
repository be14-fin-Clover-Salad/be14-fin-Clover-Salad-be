package com.clover.salad.product.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.product.command.application.dto.ProductDTO;
import com.clover.salad.product.command.application.service.ProductCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@Slf4j
@RequiredArgsConstructor
public class ProductCommandController {
	private final ProductCommandService productCSer;
	
	/* 설명. 상품 등록 */
	@PostMapping("/register")
	public ResponseEntity<String> registerProduct(@RequestBody ProductDTO productDTO) {
		productCSer.registerProduct(productDTO);
		return ResponseEntity.ok(productDTO.getName() + " is registered");
	}
	
	/* 설명. 상품 수정 */
	@PutMapping("/update/{productId}")
	public ResponseEntity<String> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductDTO productDTO) {
		productCSer.updateProduct(productId, productDTO);
		return ResponseEntity.ok(productDTO.getName() + " is updated");
	}
	
	/* 설명. 상품 삭제(논리 삭제) */
	@PutMapping("/delete/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable("productId") int productId) {
		String name = productCSer.deleteProduct(productId);
		return ResponseEntity.ok(name + " is deleted");
	}
}
