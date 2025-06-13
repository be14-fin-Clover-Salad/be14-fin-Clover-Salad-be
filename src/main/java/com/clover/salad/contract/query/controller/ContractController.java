package com.clover.salad.contract.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.contract.query.dto.ContractDTO;
import com.clover.salad.contract.query.dto.ContractResponseDTO;
import com.clover.salad.contract.query.dto.ContractSearchDTO;
import com.clover.salad.contract.query.service.ContractService;
import com.clover.salad.security.SecurityUtil;

@RestController
@RequestMapping("/api/query/contract")
public class ContractController {

	private final ContractService contractService;

	@Autowired
	public ContractController(ContractService contractService) {
		this.contractService = contractService;
	}

	@GetMapping("/{employeeId}")
	public ResponseEntity<List<ContractDTO>> find(@PathVariable int employeeId) {
		return ResponseEntity.ok(contractService.findContractInfo(employeeId));
	}

	@PostMapping("/search")
	public ResponseEntity<List<ContractDTO>> search(@RequestBody ContractSearchDTO contractSearchDTO) {
		int employeeId = SecurityUtil.getEmployeeId();
		return ResponseEntity.ok(contractService.searchContracts(employeeId, contractSearchDTO));
	}

	@GetMapping("/{contractId}/info")
	public ResponseEntity<ContractResponseDTO> findDetailInfoByContractId(@PathVariable int contractId) {
		return ResponseEntity.ok(contractService.findDetailInfo(contractId));
	}

	@GetMapping("/employees/{employeeId}/customers")
	public ResponseEntity<List<Integer>> getCustomersByEmployee(@PathVariable int employeeId) {
		return ResponseEntity.ok(contractService.getCustomerIdsByEmployee(employeeId));
	}

	@GetMapping("/validContract/{contractId}")
	public ResponseEntity<Boolean> checkContractById(@PathVariable int contractId) {
		return ResponseEntity.ok(contractService.contractValidationById(contractId));
	}
}
