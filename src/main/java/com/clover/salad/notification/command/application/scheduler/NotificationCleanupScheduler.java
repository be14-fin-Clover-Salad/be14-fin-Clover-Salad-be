package com.clover.salad.notification.command.application.scheduler;

import com.clover.salad.notification.command.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class NotificationCleanupScheduler {

	private final NotificationRepository notificationRepository;

	@Autowired
	public NotificationCleanupScheduler(NotificationRepository notificationRepository
	) {
		this.notificationRepository = notificationRepository;
	}

	/**
	 * 설명. 매달 1일 자정에 soft-deleted된 알림 중 생성일이 1개월 이상 지난 알림들을 삭제함
	 */
	@Scheduled(cron = "0 0 0 1 * *") // 매달 1일 00:00 실행
	public void cleanUpDeletedNotifications() {

		LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
		notificationRepository.deleteByIsDeletedTrueAndCreatedAtBefore(oneMonthAgo);

		log.info("Soft-deleted 알림 중 {} 이전 데이터 하드 삭제 완료", oneMonthAgo);
	}
}
