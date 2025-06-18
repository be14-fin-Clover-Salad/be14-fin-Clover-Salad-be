package com.clover.salad.product.command.application.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clover.salad.product.command.application.dto.ProductDTO;
import com.clover.salad.product.command.application.service.ProductCommandService;
import com.clover.salad.product.command.application.dto.ProductImageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/product")
@Slf4j
@RequiredArgsConstructor
public class ProductCommandController {
	private final ProductCommandService productCommandService;
	
	/* 설명. 상품 등록 */
	@PostMapping("/register")
	public ResponseEntity<String> registerProduct(@RequestBody ProductDTO productDTO) {
		productCommandService.registerProduct(productDTO);
		return ResponseEntity.ok(productDTO.getName() + " is registered");
	}
	
	/* 설명. 상품 수정 */
	@PutMapping("/update/{productId}")
	public ResponseEntity<String> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductDTO productDTO) {
		productCommandService.updateProduct(productId, productDTO);
		return ResponseEntity.ok(productDTO.getName() + " is updated");
	}
	
	/* 설명. 상품 삭제(논리 삭제) */
	@PutMapping("/delete/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable("productId") int productId) {
		String name = productCommandService.deleteProduct(productId);
		return ResponseEntity.ok(name + " is deleted");
	}
	
	/* 설명. 상품 이미지 업로드 */
	@PostMapping("/upload")
	public ResponseEntity<ProductImageDTO> uploadProductImage(@RequestParam("file") MultipartFile file) {
		ProductImageDTO productImageDTO;
		try {
			productImageDTO = productCommandService.uploadProductImage(file);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(
				new ProductImageDTO(null, "이미지 업로드 실패: " + e.getMessage(), null)
			);
		}
		return ResponseEntity.ok(productImageDTO);
	}
}
