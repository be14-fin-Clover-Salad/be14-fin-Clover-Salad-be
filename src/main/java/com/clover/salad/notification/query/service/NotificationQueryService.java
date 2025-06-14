package com.clover.salad.notification.query.service;

import com.clover.salad.notification.query.dto.NotificationDropdownResponseDTO;
import com.clover.salad.notification.query.dto.NotificationListResponseDTO;

import java.util.List;

public interface NotificationQueryService {

	/* 설명. 알림 드롭다운 */
	List<NotificationDropdownResponseDTO> getUnreadTop5();

	/* 설명. 알림 목록 */
	List<NotificationListResponseDTO> getAllNotifications(int page);


}
