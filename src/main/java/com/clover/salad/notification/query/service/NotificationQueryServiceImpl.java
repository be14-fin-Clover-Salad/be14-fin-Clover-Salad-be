package com.clover.salad.notification.query.service;

import com.clover.salad.notification.query.dto.NotificationDropdownResponseDTO;
import com.clover.salad.notification.query.mapper.NotificationMapper;
import com.clover.salad.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {
	private final NotificationMapper notificationMapper;

	public NotificationQueryServiceImpl(NotificationMapper notificationMapper
	) {
		this.notificationMapper = notificationMapper;
	}

	@Override
	public List<NotificationDropdownResponseDTO> getUnreadTop5() {
		int employeeId = SecurityUtil.getEmployeeId();
		return notificationMapper.findTop5UnreadByEmployeeId(employeeId);
	}
}
