package com.clover.salad.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

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

	// 내부에서 권한 기반으로 설정 (사용자 입력 금지)
	@JsonIgnore
	private Integer reqId;

	@JsonIgnore
	private Integer aprvId;
}