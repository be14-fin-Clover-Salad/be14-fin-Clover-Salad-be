package com.clover.salad.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String header = request.getHeader("Authorization");

		/* 설명. 예외처리 */
		// 1. Authorization 헤더가 없거나 형식이 Bearer로 시작하지 않으면 다음 필터로 넘김 (인증 생략)
		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.replace("Bearer ", "");

		// 2. 토큰이 유효하지 않은 경우 → 인증 실패 (만료, 위조 등 포함)
		if (!jwtUtil.validateToken(token)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
			return;
		}

		// 3. Redis에 블랙리스트로 등록된 토큰이면 접근 거부 (로그아웃된 토큰 등)
		String redisKey = "blacklist:" + token;
		if (redisTemplate.opsForValue().get(redisKey) != null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "블랙리스트 토큰입니다.");
			return;
		}

		// 4. 토큰이 access 용도가 아닌 경우 필터링 건너뜀 (예: refresh 토큰)
		String subject = jwtUtil.getClaims(token).getSubject();
		if (!"ERP_ACCESS".equals(subject)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			// 5. 인증 객체 생성 및 SecurityContext에 저장
			Authentication authentication = jwtUtil.getAuthentication(token);

			if (authentication == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 실패");
				return;
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 파싱 또는 인증 처리 실패");
			return;
		}

		// 7. 인증 성공 후 다음 필터로 요청 전달
		filterChain.doFilter(request, response);
	}

	/* 설명. 토큰 유효성 검사에서 /auth/login은 제외 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.equals("/auth/login") || path.equals("/auth/refresh-token"); // 예외 처리
	}
}