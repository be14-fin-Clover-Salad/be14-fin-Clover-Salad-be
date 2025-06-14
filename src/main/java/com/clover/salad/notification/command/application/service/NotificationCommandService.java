package com.clover.salad.notification.command.application.service;

import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;

public interface NotificationCommandService {
	void createNotification(NotificationCreateDTO notificationCreateDTO);
}
