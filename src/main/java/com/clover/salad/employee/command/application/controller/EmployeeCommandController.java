package com.clover.salad.employee.command.application.controller;

import com.clover.salad.employee.command.application.dto.*;
import com.clover.salad.employee.command.application.service.EmployeeCommandService;
import com.clover.salad.security.AuthService;
import com.clover.salad.security.JwtUtil;
import com.clover.salad.security.SecurityUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeCommandController {

	private final ModelMapper modelMapper;
	private final EmployeeCommandService employeeCommandService;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final AuthService authService;

	@Autowired
	public EmployeeCommandController(
		ModelMapper modelMapper,
		EmployeeCommandService employeeCommandService,
		JwtUtil jwtUtil,
		RedisTemplate<String, String> redisTemplate,
		AuthService authService
	) {
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

	@PatchMapping("/mypage")
	public ResponseEntity<String> updateEmployee(@RequestBody EmployeeUpdateDTO dto) {
		int userId = SecurityUtil.getEmployeeId();
		employeeCommandService.updateEmployee(userId, dto);
		return ResponseEntity.ok("회원 정보가 수정되었습니다.");
	}

	@PostMapping("/password-change")
	public ResponseEntity<ResponseChangePasswordDTO> changePassword(@RequestBody RequestChangePasswordDTO dto) {
		int userId = SecurityUtil.getEmployeeId();
		employeeCommandService.changePassword(userId, dto);
		return ResponseEntity.ok(new ResponseChangePasswordDTO("비밀번호가 변경되었습니다."));
	}

	@PatchMapping("/mypage/profile-path")
	public ResponseEntity<String> updateProfilePath(@RequestBody UpdateProfilePathDTO dto) {
		int userId = SecurityUtil.getEmployeeId();
		employeeCommandService.updateProfilePath(userId, dto.getPath());
		return ResponseEntity.ok("프로필 경로가 성공적으로 수정되었습니다.");
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 없음");
		}

		String refreshToken = null;
		for (Cookie cookie : cookies) {
			if ("refreshToken".equals(cookie.getName())) {
				refreshToken = cookie.getValue();
				break;
			}
		}

		if (refreshToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 누락");
		}

		if (!jwtUtil.validateToken(refreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 유효하지 않음");
		}

		int employeeId = jwtUtil.getEmployeeId(refreshToken);
		String redisKey = "refresh:" + employeeId;
		String savedToken = redisTemplate.opsForValue().get(redisKey);

		if (!refreshToken.equals(savedToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("만료되었거나 위조된 토큰");
		}

		UserDetails userDetails = authService.loadUserById(employeeId);
		String newAccessToken = jwtUtil.createAccessToken(employeeId, userDetails.getAuthorities());
		response.setHeader("Authorization", "Bearer " + newAccessToken);

		return ResponseEntity.ok("AccessToken 재발급 완료");
	}

	@PostMapping("/password-reset")
	public ResponseEntity<String> requestResetPassword(@RequestBody RequestResetPasswordDTO dto) {

		employeeCommandService.sendResetPasswordLink(dto.getCode(), dto.getEmail());
		return ResponseEntity.ok("비밀번호 재설정 링크를 이메일로 전송했습니다.");
	}

	@PostMapping("/password-resets/confirm")
	public ResponseEntity<String> confirmResetPassword(@RequestBody RequestConfirmResetPasswordDTO dto) {

		employeeCommandService.confirmResetPassword(dto.getToken(), dto.getNewPassword());
		return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
	}
}