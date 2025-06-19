package com.clover.salad.security;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clover.salad.employee.command.domain.aggregate.vo.RequestLoginVO;
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

			ObjectMapper mapper = new ObjectMapper();
			RequestLoginVO creds = mapper.readValue(body, RequestLoginVO.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getCode(), creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException {

		EmployeeDetails user = (EmployeeDetails) authResult.getPrincipal();
		int id = user.getId();
		String code = user.getCode();

		String accessToken = jwtUtil.createAccessToken(id, code, user.getAuthorities());
		String refreshToken = jwtUtil.createRefreshToken(id, code);

		redisTemplate.opsForValue().set("refresh:" + id, refreshToken, Duration.ofDays(7));
		response.setHeader("Authorization", "Bearer " + accessToken);

		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(true);
		refreshCookie.setPath("/");
		refreshCookie.setMaxAge(60 * 60 * 24 * 7);
		refreshCookie.setAttribute("SameSite", "Strict");
		response.addCookie(refreshCookie);

		LoginHeaderInfoDTO headerInfo = employeeQueryService.getLoginHeaderInfoById(id);

		response.setContentType("application/json;charset=UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), headerInfo);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException {
		log.warn("로그인 실패: {}", failed.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(),
			Map.of("message", "사번 또는 비밀번호가 올바르지 않습니다."));
	}

}
