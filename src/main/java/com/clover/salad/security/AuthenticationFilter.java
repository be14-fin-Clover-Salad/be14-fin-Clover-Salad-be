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
			RequestLoginVO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginVO.class);
			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getCode(), creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) {
		String code = ((User)authResult.getPrincipal()).getUsername();
		List<String> roles = authResult.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());

		String accessToken = jwtUtil.createAccessToken(code, authResult.getAuthorities());
		String refreshToken = jwtUtil.createRefreshToken(code);

		redisTemplate.opsForValue().set("refresh:" + code, refreshToken, Duration.ofHours(8));

		response.addHeader("Authorization", "Bearer " + accessToken);
		response.addHeader("Refresh-Token", refreshToken);
	}
}
