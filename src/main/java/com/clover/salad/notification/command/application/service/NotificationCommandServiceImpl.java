package com.clover.salad.notification.command.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;
import com.clover.salad.notification.command.domain.aggregate.entity.NotificationEntity;
import com.clover.salad.notification.command.domain.repository.NotificationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {
	private final NotificationRepository notificationRepository;

	public NotificationCommandServiceImpl(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	@Override
	public void createNotification(NotificationCreateDTO notificationCreateDTO) {
		NotificationEntity notificationEntity = NotificationEntity.builder()
			.type(notificationCreateDTO.getType())
			.content(notificationCreateDTO.getContent())
			.url(notificationCreateDTO.getUrl())
			.createdAt(LocalDateTime.now())
			.isRead(false)
			.isDeleted(false)
			.build();

		notificationRepository.save(notificationEntity);
	}
}
