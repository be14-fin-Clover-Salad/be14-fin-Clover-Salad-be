package com.clover.salad.notification.command.application.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;
import com.clover.salad.notification.command.domain.aggregate.entity.NotificationEntity;
import com.clover.salad.notification.command.domain.repository.NotificationRepository;
import com.clover.salad.security.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {
	private final NotificationRepository notificationRepository;

	@Autowired
	public NotificationCommandServiceImpl(NotificationRepository notificationRepository
	) {
		this.notificationRepository = notificationRepository;
	}

	@Override
	public void createNotification(NotificationCreateDTO notificationCreateDTO) {
		NotificationEntity notificationEntity = NotificationEntity.builder()
			.type(notificationCreateDTO.getType())
			.content(notificationCreateDTO.getContent())
			.url(notificationCreateDTO.getUrl())
			.employeeId(notificationCreateDTO.getEmployeeId())
			.createdAt(LocalDateTime.now())
			.isRead(false)
			.isDeleted(false)
			.build();

		notificationRepository.save(notificationEntity);
	}

	@Override
	public void markAsRead(int id) {
		NotificationEntity notificationEntity = notificationRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));

		if (notificationEntity.getEmployeeId() != SecurityUtil.getEmployeeId()) {
			throw new AccessDeniedException("자신의 알림만 읽음 처리할 수 있습니다.");
		}

		notificationEntity.markAsRead(); // 변경 감지 발생

		notificationRepository.save(notificationEntity);
	}
}
