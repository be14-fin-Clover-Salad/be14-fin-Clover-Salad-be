package com.clover.salad.approval.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalDecisionDTO {
	private int approvalId;
	private String decision;
	private String comment;
}
