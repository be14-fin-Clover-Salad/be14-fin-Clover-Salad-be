package com.clover.salad.notice.query.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDetailDTO {
	private int id;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	@JsonProperty("isDeleted")
	private boolean isDeleted;
	private int writerId;
	private String writerName;
	private String writerLevel;
	private String departmentName;
	private List<CheckInfoDTO> checkList;
}
