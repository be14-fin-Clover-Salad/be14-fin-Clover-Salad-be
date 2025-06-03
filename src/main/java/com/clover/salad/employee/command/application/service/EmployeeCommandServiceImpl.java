package com.clover.salad.employee.command.application.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.security.JwtUtil;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final JavaMailSender mailSender;

	@Value("${salad.frontend.reset-url}")
	private String frontendResetUrl;

	private static final long EXPIRATION_MINUTES = 5;

	@Autowired
	public EmployeeCommandServiceImpl(EmployeeRepository employeeRepository,
		ModelMapper modelMapper,
		BCryptPasswordEncoder bCryptPasswordEncoder,
		JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate,
		JavaMailSender mailSender) {
		this.employeeRepository = employeeRepository;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
		this.mailSender = mailSender;
	}

	@Override
	public void logout(String token) {
		LocalDateTime expiration = jwtUtil.getExpiration(token);
		Duration remaining = Duration.between(LocalDateTime.now(), expiration);
		if (!remaining.isNegative() && !remaining.isZero()) {
			redisTemplate.opsForValue().set("blacklist:" + token, "logout", remaining);
		}
	}

	@Override
	public void sendResetPasswordLink(String code, String email) {
		Optional<EmployeeEntity> optionalEmployee = employeeRepository.findByCode(code);

		if (optionalEmployee.isEmpty() || !optionalEmployee.get().getEmail().equals(email)) {
			throw new RuntimeException("사번 또는 이메일이 일치하지 않습니다.");
		}

		String token = UUID.randomUUID().toString();
		redisTemplate.opsForHash().put("reset_token:" + token, "email", email);

		String resetLink = frontendResetUrl + token;

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("[SALAD] 비밀번호 재설정 링크입니다.");
			helper.setText("다음 링크를 클릭해 비밀번호를 재설정하세요 (5분 내 만료):\n\n" + resetLink, false);
			helper.setFrom("teamflover@naver.com");

			mailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException("메일 전송 중 오류가 발생했습니다.", e);
		}
	}

	@Override
	public void confirmResetPassword(String token, String newPassword) {
		String redisKey = "reset_token:" + token;
		String email = redisTemplate.opsForValue().get(redisKey);
		if (email == null) {
			throw new RuntimeException("유효하지 않거나 만료된 토큰입니다.");
		}

		Optional<EmployeeEntity> optionalEmployee = employeeRepository.findByEmail(email);
		if (optionalEmployee.isEmpty()) {
			throw new RuntimeException("사용자를 찾을 수 없습니다.");
		}
		EmployeeEntity employee = optionalEmployee.get();

		employee.setEncPwd(bCryptPasswordEncoder.encode(newPassword));
		employeeRepository.save(employee);

		redisTemplate.delete(redisKey);
	}
}
