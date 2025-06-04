package com.clover.salad.security;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clover.salad.employee.command.domain.aggregate.vo.RequestLoginVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtUtil jwtUtil;

	public AuthenticationFilter(AuthenticationManager authenticationManager,
		Environment env, JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			String body = request.getReader().lines().collect(Collectors.joining());
			log.info("[AUTH FILTER] Raw request body: {}", body);

			ObjectMapper mapper = new ObjectMapper();
			RequestLoginVO creds = mapper.readValue(body, RequestLoginVO.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getCode(), creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			log.error("[AUTH FILTER] 바디 파싱 실패", e);
			throw new RuntimeException(e);
		}
	}

	// @Override
	// public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
	// 	try {
	// 		RequestLoginVO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginVO.class);
	// 		return getAuthenticationManager().authenticate(
	// 			new UsernamePasswordAuthenticationToken(creds.getCode(), creds.getPassword(), new ArrayList<>()));
	// 	} catch (IOException e) {
	// 		throw new RuntimeException(e);
	// 	}
	// }

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) {
		String code = ((User) authResult.getPrincipal()).getUsername();
		List<String> roles = authResult.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());

		// 1. AccessToken, RefreshToken 생성
		String accessToken = jwtUtil.createAccessToken(code, authResult.getAuthorities());
		String refreshToken = jwtUtil.createRefreshToken(code);

		// 2. RefreshToken Redis 저장 (로그아웃 대비용)
		redisTemplate.opsForValue().set("refresh:" + code, refreshToken, Duration.ofHours(8));

		// 3. AccessToken은 헤더에
		response.addHeader("Authorization", "Bearer " + accessToken);

		// 4. RefreshToken은 HttpOnly 쿠키에
		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setPath("/");
		refreshCookie.setMaxAge(60 * 60 * 8); // 8시간
		refreshCookie.setSecure(true);
		refreshCookie.setDomain(request.getServerName());
		refreshCookie.setAttribute("SameSite", "Strict");

		response.addCookie(refreshCookie);
	}
}
