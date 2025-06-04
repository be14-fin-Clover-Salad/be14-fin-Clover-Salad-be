package com.clover.salad.employee.command.application.controller;

import com.clover.salad.employee.command.domain.aggregate.vo.RequestLoginVO;
import com.clover.salad.employee.command.domain.aggregate.entity.EmployeeEntity;
import com.clover.salad.employee.command.domain.repository.EmployeeRepository;
import com.clover.salad.employee.query.dto.LoginHeaderInfoDTO;
import com.clover.salad.employee.query.dto.LoginResponseDTO;
import com.clover.salad.employee.query.service.EmployeeQueryService;
import com.clover.salad.security.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final EmployeeRepository employeeRepository;
	private final EmployeeQueryService employeeQueryService;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;
	private final RedisTemplate<String, String> redisTemplate;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody RequestLoginVO request,
		HttpServletResponse response) {

		// 1. 사용자 검증
		EmployeeEntity employee = employeeRepository.findByCode(request.getCode())
			.orElseThrow(() -> new RuntimeException("해당 사번을 가진 사용자가 없습니다."));

		if (!passwordEncoder.matches(request.getPassword(), employee.getEncPwd())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		// 2. 권한 포함 accessToken 생성
		UserDetails userDetails = employeeQueryService.loadUserByUsername(employee.getCode());
		String accessToken = jwtUtil.createAccessToken(userDetails.getUsername(), userDetails.getAuthorities());

		// 3. refreshToken 생성 및 Redis 저장
		String refreshToken = jwtUtil.createRefreshToken(employee.getCode());

		redisTemplate.opsForValue().set(
			"refresh:" + employee.getCode(),
			refreshToken,
			Duration.ofHours(8)
		);
		System.out.println("[Redis 저장 완료] key = refresh:" + employee.getCode() + ", value = " + refreshToken);

		// 4. refreshToken은 HttpOnly 쿠키로 전송
		ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(true)
			.sameSite("Strict")
			.path("/")
			.maxAge(Duration.ofHours(8))
			.build();
		response.addHeader("Set-Cookie", cookie.toString());

		// 5. 사용자 표시 정보 조회
		LoginHeaderInfoDTO headerInfo = employeeQueryService.getLoginHeaderInfo(employee.getCode());

		// 6. 응답 구성
		return ResponseEntity.ok(new LoginResponseDTO(accessToken, headerInfo));
	}
}