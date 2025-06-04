package com.clover.salad.security;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	public JwtFilter(JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);

			// 로그아웃된 토큰인지 확인
			if ("logout".equals(redisTemplate.opsForValue().get("blacklist:" + token))) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			// 유효한 토큰이면 SecurityContext에 등록
			if (jwtUtil.validateToken(token)) {
				Authentication authentication = jwtUtil.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}
}
