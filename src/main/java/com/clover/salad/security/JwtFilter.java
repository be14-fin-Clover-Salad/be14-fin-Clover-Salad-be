package com.clover.salad.security;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.replace("Bearer ", "");

			try {
				if (jwtUtil.validateToken(token)
					&& redisTemplate.opsForValue().get("blacklist:" + token) == null) {

					Claims claims = jwtUtil.getClaims(token);
					int id = Integer.parseInt(claims.getSubject());

					@SuppressWarnings("unchecked")
					Collection<GrantedAuthority> authorities = ((Collection<?>) claims.get("auth"))
						.stream()
						.map(Object::toString)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

					EmployeeDetails employeeDetails = new EmployeeDetails(id, "", authorities);

					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(employeeDetails, null, authorities);

					SecurityContextHolder.getContext().setAuthentication(authentication);

					log.info("[인증 성공] 사용자 ID: {}", id);
				}
			} catch (Exception e) {
				log.info("[Token 유효하지 않음] 이유: {} - {}", e.getClass().getSimpleName(), e.getMessage());
			}
		}

		filterChain.doFilter(request, response);
	}
}