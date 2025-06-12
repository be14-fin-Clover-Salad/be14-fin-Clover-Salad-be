package com.clover.salad.approval.command.application.service;

import com.clover.salad.approval.command.application.dto.ApprovalDecisionDTO;
import com.clover.salad.approval.command.application.dto.ApprovalRequestDTO;

public interface ApprovalCommandService {
	int requestApproval(ApprovalRequestDTO dto);

	String getApprovalCodeById(int approvalId);

	void decideApproval(ApprovalDecisionDTO dto);
}
