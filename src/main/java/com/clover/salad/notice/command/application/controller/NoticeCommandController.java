package com.clover.salad.notice.command.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.notice.command.application.dto.NoticeCreateRequest;
import com.clover.salad.notice.command.application.dto.NoticeUpdateRequest;
import com.clover.salad.notice.command.application.service.NoticeCommandService;
import com.clover.salad.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/support/notice")
@RequiredArgsConstructor
public class NoticeCommandController {

	private final NoticeCommandService noticeCommandService;

	// 공지 사항 등록
	@PostMapping("/create")
	public ResponseEntity<String> noticeCreate(@RequestBody  NoticeCreateRequest request){
		int writerId = SecurityUtil.getEmployeeId();
		noticeCommandService.createNotice(request, writerId);
		return ResponseEntity.ok("공지 사항 등록 완료");
	}

	// 공지 사항 수정
	@PutMapping("/edit/{noticeId}")
	public ResponseEntity<String> noticeUpdate(@PathVariable("noticeId") int noticeId,
		@RequestBody NoticeUpdateRequest request){

		int writerId = SecurityUtil.getEmployeeId();

		noticeCommandService.updateNotice(noticeId, request, writerId);
		return ResponseEntity.ok("공지 사항 수정 완료");
	}

	// 공지 사항 삭제
	@DeleteMapping("/delete/{noticeId}")
	public ResponseEntity<String> noticeDelete(@PathVariable("noticeId") int noticeId){
		int writerId = SecurityUtil.getEmployeeId();

		noticeCommandService.deleteNotice(noticeId, writerId);
		return ResponseEntity.ok("공지 사항 삭제 완료");

	}

	// 공지 사항 반응 등록
	@PutMapping("/{noticeId}/check")
	public ResponseEntity<String> noticeCheck(@PathVariable("noticeId") int noticeId) {
		int writerId = SecurityUtil.getEmployeeId();

		noticeCommandService.checkNotice(noticeId, writerId);
		return ResponseEntity.ok("공지 사항 반응 등록 완료");
	}

}
