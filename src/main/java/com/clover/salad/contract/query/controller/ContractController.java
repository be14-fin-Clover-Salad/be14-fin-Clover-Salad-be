package com.clover.salad.contract.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.service.ContractService;

@RestController
@RequestMapping("/api/query")
public class ContractController {

	private final ContractService contractService;

	@Autowired
	public ContractController(ContractService contractService) {
		this.contractService = contractService;
	}

	@PostMapping("/contract/{employeeId}")
	public ResponseEntity<List<ContractDTO>> find(@PathVariable int employeeId) {
		return ResponseEntity.ok(contractService.findContractInfo(employeeId));
	}

}
