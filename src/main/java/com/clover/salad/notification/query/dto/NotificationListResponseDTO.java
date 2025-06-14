package com.clover.salad.notification.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationListResponseDTO {
	private int id;
	private String type;
	private String content;
	private String url;
	private boolean isRead;
	private LocalDateTime createdAt;
}
