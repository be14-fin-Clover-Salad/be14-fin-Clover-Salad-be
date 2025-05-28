package com.clover.salad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.clover.salad.employee.query.service.EmployeeQueryService;

import lombok.extern.slf4j.Slf4j;

/* 설명. JWT 토큰 방식의 로그인을 구경할 때 UsernamePasswordAuthenticationToken을 처리할 프로바이더 */
/* 설명. Service 계층을 UserDetailsService 타입으로 바꾸고 옴 */
@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public JwtAuthenticationProvider(EmployeeQueryService employeeQueryService, PasswordEncoder passwordEncoder) {
		this.employeeQueryService = employeeQueryService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String code = authentication.getName();
		String pwd = (String) authentication.getCredentials();

		UserDetails userDetails = employeeQueryService.loadUserByUsername(code);

		if(!passwordEncoder.matches(pwd, userDetails.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
