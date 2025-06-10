package com.clover.salad.approval.query.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApprovalSearchResponseDTO {
	private int id;
	private String code;
	private String title;
	private String content;
	private LocalDateTime reqDate;
	private LocalDateTime aprvDate;
	private String state;
	private String comment;
	private String reqName;
	private String aprvName;
	private String contractCode;
}
