package com.clover.salad.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.Filter;

@Configuration
public class WebSecurity {

	private JwtAuthenticationProvider jwtAuthenticationProvider;
	private Environment env;
	private JwtUtil jwtUtil;

	@Autowired
	public WebSecurity(JwtAuthenticationProvider jwtAuthenticationProvider, Environment env, JwtUtil jwtUtil) {
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
		this.env = env;
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());

		http.authorizeHttpRequests(authz ->
				authz.requestMatchers(new AntPathRequestMatcher("/employee", "POST")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/employee/**", "GET")).hasRole("MANAGER")
					.anyRequest().authenticated()
			).authenticationManager(authenticationManager())

			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilter(getAuthenticationFilter(authenticationManager()));
		http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
		return new AuthenticationFilter(authenticationManager, env);
	}
}
