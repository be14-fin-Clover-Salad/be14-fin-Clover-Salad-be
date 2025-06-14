package com.clover.salad.notification.query.service;

import com.clover.salad.notification.query.dto.NotificationDropdownResponseDTO;

import java.util.List;

public interface NotificationQueryService {
	List<NotificationDropdownResponseDTO> getUnreadTop5();
}
