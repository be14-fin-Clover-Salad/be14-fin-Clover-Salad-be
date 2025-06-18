package com.clover.salad.notification.command.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationTokenServiceImpl implements NotificationTokenService {

	private final RedisTemplate<String, String> redisTemplate;

	@Autowired
	public NotificationTokenServiceImpl(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String issueToken(int employeeId) {
		String token = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set("sse:" + token, String.valueOf(employeeId), Duration.ofMinutes(1));
		return token;
	}

	@Override
	public Integer resolveEmployeeId(String token) {
		String value = redisTemplate.opsForValue().get("sse:" + token);
		if (value == null) return null;

		redisTemplate.delete("sse:" + token);
		return Integer.parseInt(value);
	}
}
