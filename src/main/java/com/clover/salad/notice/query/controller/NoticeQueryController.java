package com.clover.salad.notice.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.notice.query.dto.NoticeDTO;
import com.clover.salad.notice.query.service.NoticeQueryService;

@RestController
public class NoticeQueryController {

	private final NoticeQueryService noticeQueryService;

	@Autowired
	public NoticeQueryController(NoticeQueryService noticeQueryService) {
		this.noticeQueryService = noticeQueryService;
	}

	@GetMapping("/notice")
	public ResponseEntity<List<NoticeDTO>> findNoticeList() {
		return ResponseEntity.ok(noticeQueryService.findNoticeList());
	}

	@GetMapping("/notice/{noticeId}")
	public ResponseEntity<NoticeDTO> findNoticeDetail(@PathVariable("noticeId") int noticeId) {
		return ResponseEntity.ok(noticeQueryService.findNoticeDeatil(noticeId));
	}
}
