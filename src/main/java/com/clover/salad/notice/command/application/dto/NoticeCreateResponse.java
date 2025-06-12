package com.clover.salad.notice.command.application.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCreateResponse {
	private int id;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private boolean isDeleted;
	private int employeeId;
}
