package com.clover.salad.notification.command.application.controller;

import com.clover.salad.notification.command.domain.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/* 설명. 스케쥴러 테스트용 임시 하드 딜리트 api */
@RestController
@RequestMapping("/notification/test")
public class NotificationTestController {

	private final NotificationRepository notificationRepository;

	@Autowired
	public NotificationTestController(NotificationRepository notificationRepository
	) {
		this.notificationRepository = notificationRepository;
	}

	@Transactional
	@DeleteMapping("/cleanup")
	public ResponseEntity<String> testHardDelete() {
		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMinutes(1);
		notificationRepository.deleteByIsDeletedTrueAndCreatedAtBefore(oneMonthAgo);
		return ResponseEntity.ok("테스트용 하드 삭제 완료");
	}
}
