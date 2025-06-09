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

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.replace("Bearer ", "");

			if (jwtUtil.validateToken(token)) {
				String redisKey = "blacklist:" + token;

				if (redisTemplate.opsForValue().get(redisKey) == null) {
					String subject = jwtUtil.getClaims(token).getSubject();
					if ("ERP_ACCESS".equals(subject)) {
						Authentication authentication = jwtUtil.getAuthentication(token);
						if (authentication != null) {
							SecurityContextHolder.getContext().setAuthentication(authentication);
							log.info("[JWT FILTER] 인증 정보 등록 완료 - employeeId: {}", jwtUtil.getEmployeeId(token));
						}
					} else {
						log.info("[JWT FILTER] Refresh 토큰 등 인증 대상 아님 - subject: {}", subject);
					}
				} else {
					log.warn("[JWT FILTER] 블랙리스트에 등록된 토큰입니다: {}", token);
				}
			} else {
				log.warn("[JWT FILTER] 유효하지 않은 토큰입니다: {}", token);
			}
		}

		filterChain.doFilter(request, response);
	}
}