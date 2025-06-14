package com.clover.salad.qna.query.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaDetailDTO {
	private int id;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private String answerStatus;
	private String answerContent;
	@JsonProperty("isDeleted")
	private boolean isDeleted;
	private int writerId;
	private String writerName;
	private String writerLevel;
	private String departmentName;
}
