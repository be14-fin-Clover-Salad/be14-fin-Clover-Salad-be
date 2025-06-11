package com.clover.salad.approval.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalDecisionDTO {
	private int approvalId;
	private String decision;	// 요청에 대한 결재 상태: 승인(APPROVE), 반려(REJECT)
	private String comment;		// 반려일 경우 필수
}
