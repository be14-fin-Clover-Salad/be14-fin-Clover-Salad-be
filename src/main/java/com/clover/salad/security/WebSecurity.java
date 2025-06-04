package com.clover.salad.security;

import static org.springframework.security.config.Customizer.*;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final Environment env;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	@Autowired
	public WebSecurity(JwtAuthenticationProvider jwtAuthenticationProvider,
		Environment env,
		JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate) {
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
		this.env = env;
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		AuthenticationManager manager = authenticationManager();

		http.csrf(csrf -> csrf.disable());
		http.cors(withDefaults());

		http.authorizeHttpRequests(authz ->
				authz
					// 개발용 일시적 허용
					.requestMatchers("/**").permitAll()
					.requestMatchers("/login").permitAll()
					.requestMatchers("/employee").permitAll()
					.requestMatchers("/employee/**").permitAll()
					.anyRequest().authenticated()
			)
			.authenticationManager(manager)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilter(getAuthenticationFilter(manager));

		http.addFilterBefore(new JwtFilter(jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, env, jwtUtil, redisTemplate);
		authenticationFilter.setFilterProcessesUrl("/login");
		return authenticationFilter;
	}
}