package com.clover.salad.employee.command.application.controller;

import com.clover.salad.employee.command.application.dto.EmployeeUpdateDTO;
import com.clover.salad.employee.command.application.dto.RequestChangePasswordDTO;
import com.clover.salad.employee.command.application.dto.RequestConfirmResetPasswordDTO;
import com.clover.salad.employee.command.application.dto.RequestResetPasswordDTO;
import com.clover.salad.employee.command.application.dto.ResponseChangePasswordDTO;
import com.clover.salad.employee.command.application.dto.UpdateProfilePathDTO;
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

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = null;

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

		if (!jwtUtil.validateToken(refreshToken)) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Refresh token이 유효하지 않습니다.");
		}

		String userIdStr = jwtUtil.getUserId(refreshToken);
		int userId = Integer.parseInt(userIdStr);

		String storedToken = redisTemplate.opsForValue().get("refresh:" + userId);
		if (storedToken == null || !storedToken.equals(refreshToken)) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("refreshToken이 유효하지 않거나 만료되었습니다.");
		}

		UserDetails userDetails = authService.loadUserDetailsById(userIdStr);
		String newAccessToken = jwtUtil.createAccessToken(userId, userDetails.getAuthorities());

		response.addHeader("Authorization", "Bearer " + newAccessToken);

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/mypage")
	public ResponseEntity<String> updateEmployee(
		@RequestHeader("Authorization") String token,
		@RequestBody EmployeeUpdateDTO dto) {

		int userId = Integer.parseInt(jwtUtil.getUserId(token.replace("Bearer ", "")));
		employeeCommandService.updateEmployee(userId, dto);

		return ResponseEntity.ok("회원 정보가 수정되었습니다.");
	}

	@PostMapping("/password-change")
	public ResponseEntity<ResponseChangePasswordDTO> changePassword(
		@RequestHeader("Authorization") String token,
		@RequestBody RequestChangePasswordDTO dto) {

		int userId = Integer.parseInt(jwtUtil.getUserId(token.replace("Bearer ", "")));
		employeeCommandService.changePassword(userId, dto);

		return ResponseEntity.ok(new ResponseChangePasswordDTO("비밀번호가 변경되었습니다."));
	}

	@PatchMapping("/mypage/profile-path")
	public ResponseEntity<String> updateProfilePath(
		@RequestHeader("Authorization") String token,
		@RequestBody UpdateProfilePathDTO dto) {

		int userId = Integer.parseInt(jwtUtil.getUserId(token.replace("Bearer ", "")));
		employeeCommandService.updateProfilePath(userId, dto.getPath());

		return ResponseEntity.ok("프로필 경로가 성공적으로 수정되었습니다.");
	}
}