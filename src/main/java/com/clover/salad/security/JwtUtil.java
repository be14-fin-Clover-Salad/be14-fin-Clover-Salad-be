package com.clover.salad.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.clover.salad.employee.query.service.EmployeeQueryService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final Key key;
	private final EmployeeQueryService employeeQueryService;

	public JwtUtil(@Value("${token.secret}") String secretKey, EmployeeQueryService employeeQueryService) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.employeeQueryService = employeeQueryService;
	}

	public String createAccessToken(int id, Collection<? extends GrantedAuthority> roles) {
		return Jwts.builder()
			.setSubject(String.valueOf(id))
			.claim("auth", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(30))))
			.signWith(key)
			.compact();
	}

	public String createRefreshToken(int id) {
		return Jwts.builder()
			.setSubject(String.valueOf(id))
			.setExpiration(Date.from(Instant.now().plus(Duration.ofHours(8))))
			.signWith(key)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			log.info("[Token 유효] {}", token);
			return true;
		} catch (Exception e) {
			log.info("[Token 유효하지 않음] 이유: {} - {}", e.getClass().getSimpleName(), e.getMessage());
			return false;
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		String subject = claims.getSubject(); // id로 저장됨
		UserDetails userDetails = employeeQueryService.loadUserByUsername(subject);

		Collection<GrantedAuthority> authorities = ((Collection<?>) claims.get("auth"))
			.stream().map(Object::toString).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

	public String getUserId(String token) {
		String id = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
		log.info("[ID from token] {}", id);
		return id;
	}

	public LocalDateTime getExpiration(String token) {
		Date expirationDate = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
		LocalDateTime expiration = Instant.ofEpochMilli(expirationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		log.info("[Token 만료시간] {}", expiration);
		return expiration;
	}
}
