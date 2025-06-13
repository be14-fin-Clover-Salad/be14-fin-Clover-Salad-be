package com.clover.salad.qna.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaCreateRequest {
	private String title;
	private String content;
}
