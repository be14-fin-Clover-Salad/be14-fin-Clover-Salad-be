package com.clover.salad.consult.query.service;

import java.util.List;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;

public interface ConsultQueryService {
    ConsultQueryDTO findById(int id);

    List<ConsultQueryDTO> findAll(int page, int size);
}
