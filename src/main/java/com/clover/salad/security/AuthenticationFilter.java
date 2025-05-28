package com.clover.salad.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clover.salad.employee.command.domain.aggregate.vo.RequestLoginVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private Environment env;

	public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env) {
		super(authenticationManager);
		this.env = env;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		try {
			RequestLoginVO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginVO.class);
			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getCode(), creds.getPassword(), new ArrayList<>())
			);
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {

		log.info("로그인 성공 이후 spring security가 AUthentication 객체로 관리되어 넘어옴: {}", authResult);
		log.info("시크릿 키: {}", env.getProperty("token.secret"));

		String code = ((User)authResult.getPrincipal()).getUsername();
		log.info("회원의 아이디: {}", code);

		List<String> roles = authResult.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());

		log.info("List<String> 형태로 뽑아낸 로그인한 회원의 권한들: {}", roles);
		log.info("만료 시간: {}", env.getProperty("token.expiration_time"));

		Claims claims = Jwts.claims().setSubject(code);
		claims.put("auth", roles);

		String token = Jwts.builder()
			.setClaims(claims)
			.setExpiration(new Date(System.currentTimeMillis()
				+ Long.parseLong(env.getProperty("token.expiration_time"))))
			.signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
			.compact();

		response.addHeader("token", token);
	}
}
