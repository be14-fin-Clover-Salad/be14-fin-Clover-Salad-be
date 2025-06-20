package com.clover.salad.employee.command.application.service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clover.salad.common.exception.InvalidCurrentPasswordException;
import com.clover.salad.common.exception.InvalidEmailFormatException;
import com.clover.salad.common.exception.InvalidEmployeeInfoException;
import com.clover.salad.common.file.entity.FileUploadEntity;
import com.clover.salad.common.file.repository.FileUploadRepository;
import com.clover.salad.employee.command.application.dto.EmployeeUpdateDTO;
import com.clover.salad.employee.command.application.dto.RequestChangePasswordDTO;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.EmployeeQueryDTO;
import com.clover.salad.employee.query.mapper.EmployeeMapper;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

	private final EmployeeRepository employeeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final RedisTemplate<String, String> redisTemplate;
	private final JavaMailSender mailSender;
	private final PasswordEncoder passwordEncoder;
	private final EmployeeMapper employeeMapper;
	private final FileUploadRepository fileUploadRepository;

	@Value("${salad.frontend.reset-url}") // 예: http://localhost:5173/reset-password?token=
	private String frontendResetUrl;

	private static final long EXPIRATION_MINUTES = 5;

	@Autowired
	public EmployeeCommandServiceImpl(EmployeeRepository employeeRepository,
		BCryptPasswordEncoder bCryptPasswordEncoder,
		RedisTemplate<String, String> redisTemplate,
		JavaMailSender mailSender,
		PasswordEncoder passwordEncoder,
		EmployeeMapper employeeMapper,
		FileUploadRepository fileUploadRepository) {
		this.employeeRepository = employeeRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.redisTemplate = redisTemplate;
		this.mailSender = mailSender;
		this.passwordEncoder = passwordEncoder;
		this.employeeMapper = employeeMapper;
		this.fileUploadRepository = fileUploadRepository;
	}

	@Override
	public void sendResetPasswordLink(String code, String email) {

		Integer employeeId = employeeMapper.selectIdByCodeAndEmail(code, email);
		if (employeeId == null || employeeId == 0) {
			throw new InvalidEmployeeInfoException();
		}

		String token = UUID.randomUUID().toString();

		// 이메일 변경용 토큰을 Redis에 저장 (5분 유효)
		redisTemplate.opsForValue().set("reset_token:" + token, email, Duration.ofMinutes(EXPIRATION_MINUTES));

		String resetLink = frontendResetUrl + token;

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("[SALAD] 비밀번호 재설정 링크입니다.");

			String htmlContent = "<p>다음 링크를 클릭해 비밀번호를 재설정하세요 (5분 내 만료):</p>"
				+ "<p><a href=\"" + resetLink + "\">SALAD 비밀번호 재설정</a></p>";

			helper.setText(htmlContent, true);
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

		employee.setPassword(bCryptPasswordEncoder.encode(newPassword));
		employeeRepository.save(employee);

		redisTemplate.delete(redisKey);
	}

	@Override
	@Transactional
	public void updateEmployee(int id, EmployeeUpdateDTO dto) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 ID의 사원을 찾을 수 없습니다."));

		if (dto.getName() != null) employee.setName(dto.getName());
		if (dto.getEmail() != null) {
			if (!isValidEmail(dto.getEmail())) throw new InvalidEmailFormatException();
			employee.setEmail(dto.getEmail());
		}
		if (dto.getPhone() != null) employee.setPhone(dto.getPhone());

		employeeRepository.save(employee);
	}
	private boolean isValidEmail(String email) {
		String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
		return Pattern.matches(regex, email);
	}

	@Override
	public void changePassword(int id, RequestChangePasswordDTO dto) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 ID를 가진 사용자가 존재하지 않습니다."));

		boolean matches = passwordEncoder.matches(dto.getCurrentPassword(), employee.getPassword());
		if (!matches) throw new InvalidCurrentPasswordException();

		employee.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		employeeRepository.save(employee);
	}

	@Override
	@Transactional
	public void updateProfilePath(int id, String newPath) {
		EmployeeEntity employee = employeeRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 ID의 사원을 찾을 수 없습니다."));

		int profileId = employee.getProfile();

		FileUploadEntity file = fileUploadRepository.findById(profileId)
			.orElseThrow(() -> new RuntimeException("해당 프로필 파일을 찾을 수 없습니다."));

		file.setPath(newPath);
		fileUploadRepository.save(file);
	}

	@Override
	@Transactional
	public void updateProfile(int employeeId, int fileId) {
		EmployeeEntity employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new RuntimeException("해당 ID의 사원을 찾을 수 없습니다."));

		employee.setProfile(fileId);
		employeeRepository.save(employee);
	}

}
