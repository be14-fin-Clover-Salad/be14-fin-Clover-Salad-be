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
		log.info("[JWT FILTER] 요청 헤더 Authorization: {}", header);

		if (header == null || !header.startsWith("Bearer ")) {
			log.info("[JWT FILTER] Authorization 헤더 없음 또는 Bearer 시작 아님 - 익명 요청 처리");
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.replace("Bearer ", "");
		log.info("[JWT FILTER] 추출한 토큰: {}", token);

		if (!jwtUtil.validateToken(token)) {
			log.warn("[JWT FILTER] 유효하지 않은 토큰");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
			return;
		}

		String redisKey = "blacklist:" + token;
		if (redisTemplate.opsForValue().get(redisKey) != null) {
			log.warn("[JWT FILTER] 블랙리스트 토큰 접근 시도: {}", token);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "블랙리스트 토큰입니다.");
			return;
		}

		String subject = jwtUtil.getClaims(token).getSubject();
		log.info("[JWT FILTER] 토큰 subject: {}", subject);

		if (!"ERP_ACCESS".equals(subject)) {
			log.info("[JWT FILTER] 인증 대상 아님 (subject != ERP_ACCESS) → 통과");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Authentication authentication = jwtUtil.getAuthentication(token);
			if (authentication == null) {
				log.error("[JWT FILTER] 인증 객체 생성 실패 (authentication == null)");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 실패");
				return;
			}

			log.info("[JWT FILTER] 인증 객체 생성 성공: {}", authentication.getName());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info("[JWT FILTER] SecurityContext에 인증 객체 저장 완료");

		} catch (Exception e) {
			log.error("[JWT FILTER] 인증 처리 중 예외 발생", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 파싱 또는 인증 처리 실패");
			return;
		}

		filterChain.doFilter(request, response);
		log.info("[JWT FILTER] 필터 체인 다음으로 전달 완료");
	}

	/* 설명. 토큰 유효성 검사에서 /auth/login은 제외 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.equals("/auth/login") || path.equals("/auth/refresh-token"); // 예외 처리
	}
}