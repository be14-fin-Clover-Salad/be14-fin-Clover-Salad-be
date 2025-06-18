package com.clover.salad.security.auth;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notification")
public class NotificationAuthController {

	private final RedisTemplate<String, String> redisTemplate;
	private final JwtUtil jwtUtil;

	@Autowired
	public NotificationAuthController(RedisTemplate<String, String> redisTemplate,
		JwtUtil jwtUtil
	) {
		this.redisTemplate = redisTemplate;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/subscribe-token")
	public ResponseEntity<String> issueSubscriptionToken(HttpServletRequest request) {
		String token = jwtUtil.resolveToken(request);
		if (!jwtUtil.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		int employeeId = jwtUtil.getEmployeeId(token);
		String subscriptionToken = UUID.randomUUID().toString();

		// Redis 저장: 1분 유효
		redisTemplate.opsForValue().set("sse:" + subscriptionToken, String.valueOf(employeeId), Duration.ofMinutes(1));

		return ResponseEntity.ok(subscriptionToken);
	}
}
