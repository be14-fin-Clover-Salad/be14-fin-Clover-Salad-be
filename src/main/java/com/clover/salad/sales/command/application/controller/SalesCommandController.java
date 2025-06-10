package com.clover.salad.sales.command.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.sales.command.application.dto.SalesCommandDTO;
import com.clover.salad.sales.command.application.service.SalesCommandService;
import com.clover.salad.security.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sales")
public class SalesCommandController {

	private final SalesCommandService salesCommandService;

	@Autowired
	public SalesCommandController(SalesCommandService salesCommandService) {
		this.salesCommandService = salesCommandService;
	}

	@PostMapping
	public ResponseEntity<Integer> createSales(@RequestBody SalesCommandDTO salesCommandDTO) {
		int id = salesCommandService.createSales(salesCommandDTO);

		return ResponseEntity.ok(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSales(@PathVariable Integer id) {

		log.info("현재 사용자 권한: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());

		if (!SecurityUtil.hasRole("ROLE_ADMIN")) {
			return ResponseEntity.status(403).body("관리자만 삭제할 수 있습니다.");
		}

		salesCommandService.deleteSales(id);
		return ResponseEntity.noContent().build();	// 204 응답
	}
}
