package com.clover.salad.notification.command.application.controller;

import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;
import com.clover.salad.notification.command.application.service.NotificationCommandService;
import com.clover.salad.notification.command.domain.aggregate.enums.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationCommandController {

	private final NotificationCommandService notificationCommandService;

	@PostMapping("/test")
	public ResponseEntity<Void> createTestNotification(@RequestBody NotificationCreateDTO dto) {
		log.info("[알림 생성 요청] 타입: {}, 사원ID: {}, URL: {}", dto.getType(), dto.getEmployeeId(), dto.getUrl());

		notificationCommandService.createNotification(dto);
		log.info("[알림 생성 완료]");

		return ResponseEntity.ok().build();
	}
}
