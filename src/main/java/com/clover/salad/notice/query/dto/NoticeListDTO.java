package com.clover.salad.notice.query.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeListDTO {
	private int id;
	private String title;
	private LocalDateTime createdAt;
	private boolean isDeleted;
	private int employeeId;
}
