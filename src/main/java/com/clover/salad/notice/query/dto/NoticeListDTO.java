package com.clover.salad.notice.query.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeListDTO {
	private int id;
	private String title;
	private LocalDateTime createdAt;
	@JsonProperty("isDeleted")
	private boolean isDeleted;
	private int writerId;
	private String writerName;
	private String writerLevel;
	private String departmentName;
	@JsonProperty("isChecked")
	private boolean isChecked;
}
