package com.clover.salad.security;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
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
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final Key key;
	private final long accessTokenExpiration;
	private final long refreshTokenExpiration;
	private final EmployeeQueryService employeeQueryService;

	public JwtUtil(
		@Value("${token.secret}") String secretKey,
		@Value("${token.access-token-expiration}") long accessTokenExpiration,
		@Value("${token.refresh-token-expiration}") long refreshTokenExpiration,
		EmployeeQueryService employeeQueryService
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenExpiration = accessTokenExpiration;
		this.refreshTokenExpiration = refreshTokenExpiration;
		this.employeeQueryService = employeeQueryService;
	}

	public String createAccessToken(int employeeId, Collection<? extends GrantedAuthority> authorities) {
		String auth = authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		log.info("발급 전 권한 목록: {}", authorities);

		return Jwts.builder()
			.setSubject("ERP_ACCESS")
			.claim("employeeId", employeeId)
			.claim("auth", auth)
			.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String createRefreshToken(int employeeId) {
		return Jwts.builder()
			.setSubject("ERP_REFRESH")
			.claim("employeeId", employeeId)
			.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
			.signWith(key, SignatureAlgorithm.HS512)
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

	public int getEmployeeId(String token) {
		Claims claims = getClaims(token);
		return claims.get("employeeId", Integer.class);
	}

	public Collection<? extends GrantedAuthority> getAuthorities(String token) {
		Claims claims = getClaims(token);
		String auth = claims.get("auth", String.class);
		return Arrays.stream(auth.split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public LocalDateTime getExpiration(String token) {
		Date expirationDate = getClaims(token).getExpiration();
		return Instant.ofEpochMilli(expirationDate.getTime())
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);

		if (!"ERP_ACCESS".equals(claims.getSubject())) {
			log.warn("[JwtUtil] Access 토큰 아님 → 인증 객체 생성 안 함");
			return null;  // or throw new RuntimeException("Access 토큰 아님");
		}

		int employeeId = claims.get("employeeId", Integer.class);
		String auth = claims.get("auth", String.class);

		if (auth == null) {
			throw new RuntimeException("권한 정보(auth)가 누락된 토큰입니다.");
		}

		Collection<? extends GrantedAuthority> authorities = Arrays.stream(auth.split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		UserDetails userDetails = employeeQueryService.loadUserById(employeeId);
		return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
	}
}