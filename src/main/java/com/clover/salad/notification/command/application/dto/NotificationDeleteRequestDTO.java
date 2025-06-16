package com.clover.salad.notification.command.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NotificationDeleteRequestDTO {
	private List<Integer> deleteNotification;
}
