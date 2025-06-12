package com.clover.salad.approval.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequestDTO {
	private String title;
	private String content;
	private int contractId;
}