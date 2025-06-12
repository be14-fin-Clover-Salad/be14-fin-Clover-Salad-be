package com.clover.salad.notice.command.application.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCreateRequest {
	private String title;
	private String content;
	private List<Integer> targetEmployeeId;
}
