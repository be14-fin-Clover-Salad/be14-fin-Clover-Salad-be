package com.clover.salad.notice.command.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.notice.command.application.dto.NoticeCreateRequest;
import com.clover.salad.notice.command.application.dto.NoticeCreateResponse;
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
}
