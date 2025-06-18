package com.clover.salad.notification.command.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.notification.command.application.service.NotificationTokenService;
import com.clover.salad.security.SecurityUtil;

@RestController
@RequestMapping("/notification")
public class NotificationTokenController {
	private final NotificationTokenService notificationTokenService;

	@Autowired
	public NotificationTokenController(NotificationTokenService notificationTokenService) {
		this.notificationTokenService = notificationTokenService;
	}

	@GetMapping("/subscribe-token")
	public ResponseEntity<String> getSubscribeToken() {
		int employeeId = SecurityUtil.getEmployeeId();
		String token = notificationTokenService.issueToken(employeeId);  // Redis에 저장
		return ResponseEntity.ok(token);
	}
}
