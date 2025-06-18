package com.clover.salad.qna.query.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaListDTO {
	private int id;
	private String title;
	private LocalDateTime createdAt;
	private String answerStatus;
	@JsonProperty("isDeleted")
	private boolean isDeleted;
	private int writerId;
}
