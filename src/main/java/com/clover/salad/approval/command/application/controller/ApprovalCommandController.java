package com.clover.salad.approval.command.application.controller;

import com.clover.salad.approval.command.application.dto.ApprovalDecisionDTO;
import com.clover.salad.approval.command.application.dto.ApprovalRequestDTO;
import com.clover.salad.approval.command.application.service.ApprovalCommandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/approval")
public class ApprovalCommandController {

	private final ApprovalCommandService approvalCommandService;

	@Autowired
	public ApprovalCommandController(ApprovalCommandService approvalCommandService) {
		this.approvalCommandService = approvalCommandService;
	}

	@PostMapping("/request")
	public ResponseEntity<String> requestApproval(@RequestBody ApprovalRequestDTO dto) {
		int approvalId = approvalCommandService.requestApproval(dto);
		String code = approvalCommandService.getApprovalCodeById(approvalId);
		String message = "결재 요청이 정상적으로 처리되었습니다. 결재코드: " + code;
		return ResponseEntity.ok(message);
	}
	
	@PostMapping("/decision")
	public ResponseEntity<String> decideApproval(@RequestBody ApprovalDecisionDTO dto) {
		approvalCommandService.decideApproval(dto);
		return ResponseEntity.ok("결재 처리가 완료되었습니다.");
	}
}
