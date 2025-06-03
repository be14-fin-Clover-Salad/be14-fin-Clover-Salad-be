package com.clover.salad.employee.command.application.controller;

import com.clover.salad.employee.command.application.dto.RequestConfirmResetPasswordDTO;
import com.clover.salad.employee.command.application.dto.RequestResetPasswordDTO;
import com.clover.salad.employee.command.application.service.EmployeeCommandService;
import com.clover.salad.security.AuthService;
import com.clover.salad.security.JwtUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/employee")
public class EmployeeCommandController {

	private final ModelMapper modelMapper;
	private final EmployeeCommandService employeeCommandService;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final AuthService authService;


	@Autowired
	public EmployeeCommandController(ModelMapper modelMapper,
		EmployeeCommandService employeeCommandService,
		JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate,
		AuthService authService) {
		this.modelMapper = modelMapper;
		this.employeeCommandService = employeeCommandService;
		this.jwtUtil = jwtUtil;
		this.redisTemplate = redisTemplate;
		this.authService = authService;
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
		String pureToken = token.replace("Bearer ", "");
		employeeCommandService.logout(pureToken);
		return ResponseEntity.ok("로그아웃 성공 (토큰 블랙리스트 등록 완료)");
	}

	@PostMapping("/password-reset-requests")
	public ResponseEntity<String> requestResetPassword(@RequestBody RequestResetPasswordDTO dto) {
		employeeCommandService.sendResetPasswordLink(dto.getCode(), dto.getEmail());
		return ResponseEntity.ok("비밀번호 재설정 링크를 이메일로 전송했습니다.");
	}

	@PostMapping("/confirm-reset-password")
	public ResponseEntity<String> confirmResetPassword(@RequestBody RequestConfirmResetPasswordDTO dto) {
		employeeCommandService.confirmResetPassword(dto.getToken(), dto.getNewPassword());
		return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = null;

		// 1. 쿠키에서 refreshToken 추출
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("refreshToken".equals(cookie.getName())) {
					refreshToken = cookie.getValue();
				}
			}
		}

		if (refreshToken == null) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Refresh token이 없습니다.");
		}

		// 2. 유효성 검증
		if (!jwtUtil.validateToken(refreshToken)) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Refresh token이 유효하지 않습니다.");
		}

		// 3. 사용자 코드 가져오기
		String code = jwtUtil.getUsername(refreshToken);

		// 4. Redis에 저장된 refreshToken과 비교
		String storedToken = redisTemplate.opsForValue().get("refresh:" + code);
		if (storedToken == null || !storedToken.equals(refreshToken)) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("refreshToken이 유효하지 않거나 만료되었습니다.");
		}

		// 5. 새로운 accessToken 발급
		UserDetails userDetails = authService.loadUserDetails(code);
		String newAccessToken = jwtUtil.createAccessToken(code, userDetails.getAuthorities());

		// 6. Authorization 헤더로 반환
		response.addHeader("Authorization", "Bearer " + newAccessToken);

		return ResponseEntity.ok().build();
	}
}
