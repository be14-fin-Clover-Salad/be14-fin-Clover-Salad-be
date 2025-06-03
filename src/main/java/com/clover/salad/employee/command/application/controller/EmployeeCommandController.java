package com.clover.salad.employee.command.application.controller;

import com.clover.salad.employee.command.application.dto.RequestConfirmResetPasswordDTO;
import com.clover.salad.employee.command.application.dto.RequestResetPasswordDTO;
import com.clover.salad.employee.command.application.service.EmployeeCommandService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeCommandController {

	private final ModelMapper modelMapper;
	private final EmployeeCommandService employeeCommandService;

	@Autowired
	public EmployeeCommandController(ModelMapper modelMapper,
		EmployeeCommandService employeeCommandService) {
		this.modelMapper = modelMapper;
		this.employeeCommandService = employeeCommandService;
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
		String pureToken = token.replace("Bearer ", "");
		employeeCommandService.logout(pureToken);
		return ResponseEntity.ok("로그아웃 성공 (토큰 블랙리스트 등록 완료)");
	}

	@PostMapping("/request-reset-password")
	public ResponseEntity<String> requestResetPassword(@RequestBody RequestResetPasswordDTO dto) {
		employeeCommandService.sendResetPasswordLink(dto.getCode(), dto.getEmail());
		return ResponseEntity.ok("비밀번호 재설정 링크를 이메일로 전송했습니다.");
	}

	@PostMapping("/confirm-reset-password")
	public ResponseEntity<String> confirmResetPassword(@RequestBody RequestConfirmResetPasswordDTO dto) {
		employeeCommandService.confirmResetPassword(dto.getToken(), dto.getNewPassword());
		return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
	}
}
