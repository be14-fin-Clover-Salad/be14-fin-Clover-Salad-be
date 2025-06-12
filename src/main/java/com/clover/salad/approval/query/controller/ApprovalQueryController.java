package com.clover.salad.approval.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.approval.query.dto.ApprovalDetailDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchRequestDTO;
import com.clover.salad.approval.query.dto.ApprovalSearchResponseDTO;
import com.clover.salad.approval.query.service.ApprovalQueryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/approval")
public class ApprovalQueryController {

	private final ApprovalQueryService approvalQueryService;

	@Autowired
	public ApprovalQueryController(ApprovalQueryService approvalQueryService) {
		this.approvalQueryService = approvalQueryService;
	}

	@PostMapping("/search")
	public ResponseEntity<List<ApprovalSearchResponseDTO>> searchApprovals(
		@RequestBody ApprovalSearchRequestDTO requestDTO) {
		List<ApprovalSearchResponseDTO> results = approvalQueryService.searchApprovals(requestDTO);
		return ResponseEntity.ok(results);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApprovalDetailDTO> getApprovalDetail(@PathVariable int id) {
		ApprovalDetailDTO dto = approvalQueryService.getApprovalDetailById(id);
		return ResponseEntity.ok(dto);
	}
}
