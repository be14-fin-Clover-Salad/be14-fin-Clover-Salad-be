package com.clover.salad.notification.command.application.dto;

import com.clover.salad.notification.command.domain.aggregate.enums.NotificationType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationCreateDTO {
	private NotificationType type;
	private String content;
	private String url;
	private int employeeId;
}
