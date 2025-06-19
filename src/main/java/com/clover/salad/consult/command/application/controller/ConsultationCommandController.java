package com.clover.salad.consult.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.consult.command.application.dto.ConsultationCreateRequest;
import com.clover.salad.consult.command.application.service.ConsultationCommandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/consult")
@RequiredArgsConstructor
public class ConsultationCommandController {

    private final ConsultationCommandService consultationCommandService;

    @PostMapping
    public ResponseEntity<String> createConsultation(
            @RequestBody @Valid ConsultationCreateRequest request) {

        consultationCommandService.createConsultation(request);
        return ResponseEntity.ok("상담이 성공적으로 등록되었습니다.");
    }

}
