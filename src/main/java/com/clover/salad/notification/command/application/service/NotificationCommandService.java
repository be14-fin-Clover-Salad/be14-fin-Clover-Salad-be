package com.clover.salad.notification.command.application.service;

import java.util.List;

import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;

public interface NotificationCommandService {
	void createNotification(NotificationCreateDTO notificationCreateDTO);

	void markAsRead(int id);

	void softDeleteNotifications(List<Integer> deleteNotification);
}
