package com.clover.salad.notification.query.controller;

import com.clover.salad.notification.query.dto.NotificationDropdownResponseDTO;
import com.clover.salad.notification.query.service.NotificationQueryService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationQueryController {

	private final NotificationQueryService notificationQueryService;

	@Autowired
	public NotificationQueryController(NotificationQueryService notificationQueryService
	) {
		this.notificationQueryService = notificationQueryService;
	}

	@GetMapping("/unread-latest")
	public ResponseEntity<List<NotificationDropdownResponseDTO>> getUnreadDropdown() {
		List<NotificationDropdownResponseDTO> notifications = notificationQueryService.getUnreadTop5();
		return ResponseEntity.ok(notifications);
	}
}
