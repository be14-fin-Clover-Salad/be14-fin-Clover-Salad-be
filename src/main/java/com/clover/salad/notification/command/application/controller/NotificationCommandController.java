package com.clover.salad.notification.command.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.notification.command.application.service.NotificationCommandService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationCommandController {

	private final NotificationCommandService notificationCommandService;

	@Autowired
	public NotificationCommandController(NotificationCommandService notificationCommandService) {
		this.notificationCommandService = notificationCommandService;
	}

	@PatchMapping("/{id}/read")
	public ResponseEntity<String> markAsRead(@PathVariable("id") int id) {
		notificationCommandService.markAsRead(id);
		return ResponseEntity.ok("알림을 읽었습니다.");
	}
}
