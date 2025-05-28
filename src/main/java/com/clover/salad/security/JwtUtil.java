package com.clover.salad.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.clover.salad.employee.query.service.EmployeeQueryService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final Key key;
	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public JwtUtil(
		@Value("${token.secret}") String secretKey,
		EmployeeQueryService employeeQueryService) {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.employeeQueryService = employeeQueryService;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("유효하지 않은 JWT Token");
		} catch (ExpiredJwtException e) {
			log.info("만료기간이 지남");
		} catch (IllegalArgumentException e) {
			log.info("토큰의 클레임이 비어있음");
		}
		return false;
	}

	public Authentication getAuthentication(String token) {

		Claims claims = parseClaims(token);
		UserDetails userDetails = employeeQueryService.loadUserByUsername(claims.getSubject());

		/* 필기. 해당 유저의 권한들 */
		Collection<GrantedAuthority> authorities = null;

		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다");
		} else {
			authorities =
				Arrays.stream(claims.get("auth").toString()
						.replace("[", "")
						.replace("]", "")
						.split(", "))
					.map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList());
		}

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
