package com.clover.salad.employee.command.application.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.clover.salad.security.JwtUtil;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;

@Service
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;


	@Autowired
	public EmployeeCommandServiceImpl(EmployeeRepository employeeRepository,
		ModelMapper modelMapper,
		BCryptPasswordEncoder bCryptPasswordEncoder,
		JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate) {
		this.employeeRepository = employeeRepository;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void logout(String token) {
		LocalDateTime expiration = jwtUtil.getExpiration(token);
		Duration remaining = Duration.between(LocalDateTime.now(), expiration);
		if (!remaining.isNegative() && !remaining.isZero()) {
			redisTemplate.opsForValue().set("blacklist:" + token, "logout", remaining);
		}
	}
}