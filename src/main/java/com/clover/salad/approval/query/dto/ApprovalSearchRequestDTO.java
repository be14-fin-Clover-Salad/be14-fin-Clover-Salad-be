package com.clover.salad.approval.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApprovalSearchRequestDTO {

	private String code;
	private String title;
	private String content;

	private String reqDateFrom;
	private String reqDateTo;

	private String aprvDateFrom;
	private String aprvDateTo;

	private String state;
	private String comment;

	private String reqName;
	private String aprvName;
	private String contractCode;
}