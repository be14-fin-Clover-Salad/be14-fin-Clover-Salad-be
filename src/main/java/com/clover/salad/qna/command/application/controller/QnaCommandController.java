package com.clover.salad.qna.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.qna.command.application.dto.QnaAnswerRequest;
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

	// 문의 사항 삭제
	@DeleteMapping("/delete/{qnaId}")
	public ResponseEntity<String> qnaDelete(@PathVariable("qnaId") int qnaId){
		int writerId = SecurityUtil.getEmployeeId();

		qnaCommandService.deleteQna(qnaId,writerId);
		return ResponseEntity.ok("문의 사항 삭제 완료");
	}

	// 문의 사항 답변 등록
	@PostMapping("{qnaId}/answer")
	public ResponseEntity<String> qnaAnswer(@PathVariable("qnaId") int qnaId,
		@RequestBody QnaAnswerRequest request){
		int writerId = SecurityUtil.getEmployeeId();

		qnaCommandService.answerQna(qnaId, request, writerId);
		return ResponseEntity.ok("문의 사항 답변 등록 완료");
	}

	// 문의 사항 답변 수정
	@PutMapping("{qnaId}/answer")
	public ResponseEntity<String> qnaUpdate(@PathVariable("qnaId") int qnaId,
		@RequestBody QnaAnswerRequest request) {
		int writerId = SecurityUtil.getEmployeeId();

		qnaCommandService.updateAnswer(qnaId, request, writerId);
		return ResponseEntity.ok("문의 사항 답변 수정 완료");
	}


}
