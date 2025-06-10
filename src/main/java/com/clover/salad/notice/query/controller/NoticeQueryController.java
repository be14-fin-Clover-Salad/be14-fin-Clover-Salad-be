package com.clover.salad.notice.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.notice.query.dto.NoticeDetailDTO;
import com.clover.salad.notice.query.dto.NoticeListDTO;
import com.clover.salad.notice.query.service.NoticeQueryService;
import com.clover.salad.security.SecurityUtil;

@RequestMapping("/support")
@RestController
public class NoticeQueryController {

	private final NoticeQueryService noticeQueryService;

	@Autowired
	public NoticeQueryController(NoticeQueryService noticeQueryService) {
		this.noticeQueryService = noticeQueryService;
	}

	@GetMapping("/notice")
	public ResponseEntity<List<NoticeListDTO>> findNoticeList() {
		int employeeId = SecurityUtil.getEmployeeId();

		return ResponseEntity.ok(noticeQueryService.findNoticeList(employeeId));
	}

	@GetMapping("/notice/{noticeId}")
	public ResponseEntity<NoticeDetailDTO> getNoticeDetail(@PathVariable("noticeId") int noticeId) {
		int employeeId = SecurityUtil.getEmployeeId();
		NoticeDetailDTO detail = noticeQueryService.getNoticeDetail(noticeId, employeeId);

		if(detail == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(detail);
	}
}
