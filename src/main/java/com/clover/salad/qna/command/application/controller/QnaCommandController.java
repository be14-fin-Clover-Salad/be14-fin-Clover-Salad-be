package com.clover.salad.qna.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.qna.command.application.dto.QnaCreateRequest;
import com.clover.salad.qna.command.application.service.QnaCommandService;
import com.clover.salad.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/support/qna")
@RequiredArgsConstructor
public class QnaCommandController {

	private final QnaCommandService qnaCommandService;

	// 문의 사항 등록
	@PostMapping("/create")
	public ResponseEntity<String> qnaCreate(@RequestBody QnaCreateRequest request){
		int writerId = SecurityUtil.getEmployeeId();
		qnaCommandService.createQna(request, writerId);
		return ResponseEntity.ok("문의 사항 등록 완료");
	}


}
