package com.clover.salad.security;

import static org.springframework.security.config.Customizer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clover.salad.employee.query.service.EmployeeQueryService;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	private final Environment env;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final EmployeeQueryService employeeQueryService;

	@Autowired
	public WebSecurity(Environment env, JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate,
		EmployeeQueryService employeeQueryService) {
		this.env = env;
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
		this.employeeQueryService = employeeQueryService;
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.cors(withDefaults());

		http.authorizeHttpRequests(authz ->
				authz
					// 개발용 일시적 허용
					// .requestMatchers("/**").permitAll()
					.requestMatchers("/auth/login").permitAll()
					.requestMatchers("/employee").permitAll()
					.requestMatchers("/employee/**").permitAll()
					.requestMatchers("/notification/subscribe").permitAll()
					.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilter(getAuthenticationFilter(authenticationManager));
		http.addFilterBefore(new JwtFilter(jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
		AuthenticationFilter authenticationFilter =
			new AuthenticationFilter(authenticationManager, env, jwtUtil, redisTemplate, employeeQueryService);
		authenticationFilter.setFilterProcessesUrl("/auth/login");
		return authenticationFilter;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
