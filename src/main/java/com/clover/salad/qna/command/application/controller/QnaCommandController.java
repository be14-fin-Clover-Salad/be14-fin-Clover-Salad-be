package com.clover.salad.qna.command.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.qna.command.application.service.QnaCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/support/qna")
@RequiredArgsConstructor
public class QnaCommandController {

	private final QnaCommandService qnaCommandService;

	// 문의 사항 등록



}
