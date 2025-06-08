package com.clover.salad.consult.query.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;
import com.clover.salad.consult.query.service.ConsultQueryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consult")
@RequiredArgsConstructor
public class ConsultQueryController {

    private final ConsultQueryService consultQueryService;

    // 로그인한 사용자의 권한에 따라 상담 데이터 조회 (ADMIN / MANAGER / MEMBER 분기)
    @GetMapping
    public ResponseEntity<List<ConsultQueryDTO>> findByRole(HttpServletRequest request) {
        String token = extractToken(request);
        return ResponseEntity.ok(consultQueryService.findByRole(token));
    }

    // 전체 상담 데이터 조회 (is_deleted = true 포함, 관리자 전용)
    @GetMapping("/all")
    public ResponseEntity<List<ConsultQueryDTO>> findAll(HttpServletRequest request) {
        String token = extractToken(request);
        return ResponseEntity.ok(consultQueryService.findAll(token));
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
