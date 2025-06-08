package com.clover.salad.consult.query.controller;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;
import com.clover.salad.consult.query.service.ConsultQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consult")
@RequiredArgsConstructor
public class ConsultQueryController {

    private final ConsultQueryService consultService;

    private String getToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }

    // ADMIN
    @GetMapping
    public ResponseEntity<List<ConsultQueryDTO>> findAll(HttpServletRequest req) {
        return ResponseEntity.ok(consultService.findAll(getToken(req)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultQueryDTO> findById(HttpServletRequest req, @PathVariable int id) {
        return ResponseEntity.ok(consultService.findById(getToken(req), id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ConsultQueryDTO>> findAllActive(HttpServletRequest req) {
        return ResponseEntity.ok(consultService.findAllActive(getToken(req)));
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<ConsultQueryDTO> findActiveById(HttpServletRequest req,
            @PathVariable int id) {
        return ResponseEntity.ok(consultService.findActiveById(getToken(req), id));
    }

    // MANAGER
    @GetMapping("/department")
    public ResponseEntity<List<ConsultQueryDTO>> findByDepartment(HttpServletRequest req) {
        return ResponseEntity.ok(consultService.findByDepartmentName(getToken(req)));
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<ConsultQueryDTO> findByDepartmentAndId(HttpServletRequest req,
            @PathVariable int id) {
        return ResponseEntity.ok(consultService.findByDepartmentNameAndId(getToken(req), id));
    }

    // MEMBER
    @GetMapping("/employee")
    public ResponseEntity<List<ConsultQueryDTO>> findByEmployee(HttpServletRequest req) {
        return ResponseEntity.ok(consultService.findByEmployeeCode(getToken(req)));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<ConsultQueryDTO> findByEmployeeAndId(HttpServletRequest req,
            @PathVariable int id) {
        return ResponseEntity.ok(consultService.findByEmployeeCodeAndId(getToken(req), id));
    }
}
