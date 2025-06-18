package com.clover.salad.notification.command.domain.aggregate.entity;

import com.clover.salad.notification.command.domain.aggregate.enums.NotificationType;
import com.clover.salad.notification.command.domain.aggregate.converter.NotificationTypeConverter;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Convert(converter = NotificationTypeConverter.class)
	@Column(name = "type", nullable = false)
	private NotificationType type;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "is_read", nullable = false)
	private boolean isRead;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	@Column(name = "employee_id", nullable = false)
	private int employeeId;

	/* 설명. 읽음 처리 - setter 대신 명시적인 도메인 메서드 사용 */
	public void markAsRead() {
		if (!this.isRead) {
			this.isRead = true;
		}
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}
}
