package com.clover.salad.notification.command.application.service;

public interface NotificationTokenService {
	String issueToken(int employeeId);  // 일회용 토큰 발급
	Integer resolveEmployeeId(String token);  // 토큰으로 employeeId 복원 (사용 후 삭제)
}
