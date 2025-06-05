package com.clover.salad.security;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clover.salad.employee.command.domain.aggregate.vo.RequestLoginVO;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
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
	private final EmployeeQueryService employeeQueryService;

	public AuthenticationFilter(AuthenticationManager authenticationManager,
		Environment env, JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate,
		EmployeeQueryService employeeQueryService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
		this.employeeQueryService = employeeQueryService;
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
		Authentication authResult) throws IOException {

		String code = ((User) authResult.getPrincipal()).getUsername();

		// 1. AccessToken, RefreshToken 생성
		String accessToken = jwtUtil.createAccessToken(code, authResult.getAuthorities());
		String refreshToken = jwtUtil.createRefreshToken(code);

		// 2. Redis 저장
		redisTemplate.opsForValue().set("refresh:" + code, refreshToken, Duration.ofDays(7));

		// 3. Header + 쿠키 세팅
		response.setHeader("Authorization", "Bearer " + accessToken);

		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(true);
		refreshCookie.setPath("/");
		refreshCookie.setMaxAge(60 * 60 * 24 * 7);
		refreshCookie.setAttribute("SameSite", "Strict");
		response.addCookie(refreshCookie);

		// ✅ 4. 사용자 정보 조회 및 응답
		LoginHeaderInfoDTO headerInfo = employeeQueryService.getLoginHeaderInfo(code);

		response.setContentType("application/json;charset=UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), headerInfo);

		log.info("🟢 로그인 성공 - 사용자 정보 응답 완료: {}", headerInfo.getName());
	}
}
