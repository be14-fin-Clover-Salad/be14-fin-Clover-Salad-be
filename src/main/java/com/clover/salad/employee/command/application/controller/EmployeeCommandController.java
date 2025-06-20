package com.clover.salad.employee.command.application.controller;

import com.clover.salad.employee.command.application.dto.*;
import com.clover.salad.employee.command.application.service.EmployeeCommandService;
import com.clover.salad.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeCommandController {

	private final EmployeeCommandService employeeCommandService;

	@Autowired
	public EmployeeCommandController(
		EmployeeCommandService employeeCommandService
	) {
		this.employeeCommandService = employeeCommandService;
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

	@PatchMapping("/mypage/profile")
	public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileFileDTO updateProfileFileDTO) {
		int employeeId = SecurityUtil.getEmployeeId();
		employeeCommandService.updateProfile(employeeId, updateProfileFileDTO.getFileId());
		return ResponseEntity.ok("프로필이 성공적으로 변경되었습니다.");
	}
}
