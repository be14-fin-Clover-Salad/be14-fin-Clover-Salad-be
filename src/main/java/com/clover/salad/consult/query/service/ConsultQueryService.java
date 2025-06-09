package com.clover.salad.consult.query.service;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;

import java.util.List;

public interface ConsultQueryService {
    List<ConsultQueryDTO> findAll(String token);

    ConsultQueryDTO findById(String token, int id);

    List<ConsultQueryDTO> findAllActive(String token);

    ConsultQueryDTO findActiveById(String token, int id);

    List<ConsultQueryDTO> findByDepartmentName(String token);

    ConsultQueryDTO findByDepartmentNameAndId(String token, int id);

    List<ConsultQueryDTO> findByEmployeeCode(String token);

    ConsultQueryDTO findByEmployeeCodeAndId(String token, int id);
}
