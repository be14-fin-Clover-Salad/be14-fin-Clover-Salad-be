package com.clover.salad.security.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.clover.salad.common.exception.AuthException;
import com.clover.salad.security.EmployeeDetails;
import com.clover.salad.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final AuthService authService;

	@Autowired
	public AuthController(JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate,
		AuthService authService) {
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
		this.authService = authService;
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(
		@RequestHeader("Authorization") String token,
		HttpServletResponse response
	) {
		String pureToken = token.replace("Bearer ", "");

		if (!jwtUtil.validateToken(pureToken)) {
			return unauthorized("유효하지 않은 토큰입니다.");
		}

		int userId = jwtUtil.getEmployeeId(pureToken);
		blacklistAccessToken(pureToken);
		deleteRefreshToken(userId);
		expireRefreshCookie(response);

		return ResponseEntity.ok("로그아웃이 완료되었습니다.");
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<String> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = extractRefreshTokenOrThrow(request);
		validateRefreshToken(refreshToken);
		int employeeId = extractEmployeeIdOrThrow(refreshToken);
		ensureTokenMatchesRedis(refreshToken, employeeId);

		EmployeeDetails employeeDetails = (EmployeeDetails) authService.loadUserById(employeeId);
		String newAccessToken = jwtUtil.createAccessToken(
			employeeId,
			employeeDetails.getCode(),
			employeeDetails.getAuthorities()
		);

		response.setHeader("Authorization", "Bearer " + newAccessToken);
		return ResponseEntity.ok("AccessToken 재발급 완료");
	}

	/* 설명. 로그아웃 메서드 */
	// 401 Unauthorized 응답을 반환하기 위한 메서드
	private ResponseEntity<String> unauthorized(String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
	}

	// 액세스 토큰을 Redis 블랙리스트에 등록 (남은 만료 시간만큼 유지)
	private void blacklistAccessToken(String token) {
		LocalDateTime expiration = jwtUtil.getExpiration(token);
		Duration remaining = Duration.between(LocalDateTime.now(), expiration);

		if (remaining.isNegative() || remaining.isZero())
			return;

		redisTemplate.opsForValue().set("blacklist:" + token, "logout", remaining);
	}

	// 해당 유저의 refresh token을 Redis에서 삭제
	private void deleteRefreshToken(int userId) {
		redisTemplate.delete("refresh:" + userId);
	}

	// 클라이언트의 refreshToken 쿠키를 즉시 만료시킴
	private void expireRefreshCookie(HttpServletResponse response) {
		Cookie expiredCookie = new Cookie("refreshToken", null);
		expiredCookie.setPath("/");
		expiredCookie.setHttpOnly(true);
		expiredCookie.setSecure(true);
		expiredCookie.setMaxAge(0);
		expiredCookie.setAttribute("SameSite", "Strict");
		response.addCookie(expiredCookie);
	}

	/* 설명. 액세스토큰 재발급 메서드 */
	// 쿠키에서 refreshToken 추출
	private String extractRefreshTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;

		return Arrays.stream(cookies)
			.filter(cookie -> "refreshToken".equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}

	/* 설명. 액세스 토큰 재발급 예외처리 */
	private String extractRefreshTokenOrThrow(HttpServletRequest request) {
		return Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
			.filter(cookie -> "refreshToken".equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElseThrow(() -> new AuthException("RefreshToken 누락"));
	}

	private void validateRefreshToken(String token) {
		if (!jwtUtil.validateToken(token)) {
			throw new AuthException("토큰 유효하지 않음");
		}
	}

	private int extractEmployeeIdOrThrow(String token) {
		try {
			return jwtUtil.getEmployeeId(token);
		} catch (Exception e) {
			throw new AuthException("employeeId 추출 실패", HttpStatus.BAD_REQUEST);
		}
	}

	private void ensureTokenMatchesRedis(String refreshToken, int employeeId) {
		String savedToken = redisTemplate.opsForValue().get("refresh:" + employeeId);
		if (!refreshToken.equals(savedToken)) {
			throw new AuthException("만료되었거나 위조된 토큰");
		}
	}
}