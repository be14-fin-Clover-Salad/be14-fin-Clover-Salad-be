package com.clover.salad.notification.command.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.clover.salad.notification.command.application.dto.NotificationCreateDTO;
import com.clover.salad.notification.command.domain.aggregate.entity.NotificationEntity;
import com.clover.salad.notification.command.domain.repository.NotificationRepository;
import com.clover.salad.notification.query.sse.SseEmitterManager;
import com.clover.salad.security.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {
	private final NotificationRepository notificationRepository;
	private final SseEmitterManager sseEmitterManager;

	@Autowired
	public NotificationCommandServiceImpl(NotificationRepository notificationRepository,
		SseEmitterManager sseEmitterManager
	) {
		this.notificationRepository = notificationRepository;
		this.sseEmitterManager = sseEmitterManager;
	}

	@Override
	public void createNotification(NotificationCreateDTO notificationCreateDTO) {
		log.info("[알림 생성] 수신자 employeeId: {}", notificationCreateDTO.getEmployeeId());
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

		// SSE 전송
		log.info("[알림 생성] DB 저장 완료. SSE 전송 시작...");
		sseEmitterManager.send(notificationCreateDTO.getEmployeeId(), notificationCreateDTO);
	}

	@Override
	public void markAsRead(int id) {
		NotificationEntity notificationEntity = notificationRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));

		if (notificationEntity.getEmployeeId() != SecurityUtil.getEmployeeId()) {
			throw new AccessDeniedException("자신의 알림만 읽음 처리할 수 있습니다.");
		}

		notificationEntity.markAsRead();

		notificationRepository.save(notificationEntity);
	}

	@Override
	public void softDeleteNotifications(List<Integer> deleteNotification) {
		int employeeId = SecurityUtil.getEmployeeId();
		List<NotificationEntity> notifications = notificationRepository.findAllById(deleteNotification);

		for (NotificationEntity n : notifications) {
			if (n.getEmployeeId() != employeeId) {
				throw new AccessDeniedException("본인의 알림만 삭제할 수 있습니다.");
			}
			n.markAsDeleted(); // 소프트 딜리트
		}

		notificationRepository.saveAll(notifications);
	}
}
