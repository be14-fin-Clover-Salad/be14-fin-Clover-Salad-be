package com.clover.salad.security;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.security.token.TokenPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
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

	public String createAccessToken(int employeeId, String code, Collection<? extends GrantedAuthority> authorities) {
		String auth = authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		return Jwts.builder()
			.setSubject("ERP_ACCESS") 				// access 토큰임을 구분하기 위한 subject
			.claim("employeeId", employeeId) 	// 내부 식별자
			.claim("code", code)             	// 사번 (도메인 식별자)
			.claim("auth", auth)             	// 권한
			.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String createRefreshToken(int employeeId, String code) {
		return Jwts.builder()
			.setSubject("ERP_REFRESH")
			.claim("employeeId", employeeId)
			.claim("code", code)
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

		log.info("[토큰 클레임 파싱] employeeId={}, code={}, auth={}, subject={}",
			claims.get("employeeId", Object.class),
			claims.get("code", Object.class),
			claims.get("auth", Object.class),
			claims.getSubject()
		);

		if (!"ERP_ACCESS".equals(claims.getSubject())) {
			return null;
		}

		Integer employeeId = claims.get("employeeId", Integer.class);
		String code = claims.get("code", String.class);
		String auth = claims.get("auth", String.class);

		if (employeeId == null || code == null || auth == null) {
			throw new RuntimeException("JWT 클레임의 정보가 누락되었습니다. (employeeId/code/auth)");
		}

		Collection<? extends GrantedAuthority> authorities = Arrays.stream(auth.split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		TokenPrincipal principal = new TokenPrincipal(employeeId, code, authorities);
		return new UsernamePasswordAuthenticationToken(principal, null, authorities);
	}

	// 헤더에서 토큰 파싱해주는 메서드
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
