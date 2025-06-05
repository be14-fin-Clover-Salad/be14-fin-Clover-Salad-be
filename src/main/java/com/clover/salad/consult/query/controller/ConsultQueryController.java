package com.clover.salad.consult.query.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;
import com.clover.salad.consult.query.service.ConsultQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consult")
@RequiredArgsConstructor
public class ConsultQueryController {

    private final ConsultQueryService consultQueryService;

    @GetMapping("/{id}")
    public ConsultQueryDTO findById(@PathVariable int id) {
        return consultQueryService.findById(id);
    }

    @GetMapping
    public List<ConsultQueryDTO> findAll(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return consultQueryService.findAll(page, size);
    }
}
