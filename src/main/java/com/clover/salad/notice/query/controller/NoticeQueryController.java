package com.clover.salad.notice.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.notice.query.dto.NoticeQueryDTO;
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
	public ResponseEntity<List<NoticeQueryDTO>> findNoticeList() {
		int employeeId = SecurityUtil.getEmployeeId();

		if (!SecurityUtil.hasRole("ROLE_ADMIN") && !SecurityUtil.hasRole("ROLE_MANAGER")) {
			return ResponseEntity.status(403).body("회원 정보 수정 권한이 없습니다.");
		}

		return ResponseEntity.ok(noticeQueryService.findNoticeList(employeeId));
	}

	// @GetMapping("/notice/{noticeId}")
	// public ResponseEntity<NoticeQueryDTO> findNoticeDetail(@PathVariable("noticeId") int noticeId) {
	// 	return ResponseEntity.ok(noticeQueryService.findNoticeDeatil(noticeId));
	// }
}
