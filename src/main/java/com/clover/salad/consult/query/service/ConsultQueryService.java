package com.clover.salad.consult.query.service;

import java.util.List;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;

public interface ConsultQueryService {
    List<ConsultQueryDTO> findByRole(String token);

    List<ConsultQueryDTO> findAll(String token);
}
