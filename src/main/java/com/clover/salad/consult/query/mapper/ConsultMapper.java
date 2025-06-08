package com.clover.salad.consult.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;

@Mapper
public interface ConsultMapper {
    List<ConsultQueryDTO> findAllActive();

    List<ConsultQueryDTO> findByDepartmentName(String departmentName);

    List<ConsultQueryDTO> findByEmployeeName(String employeeName);

    List<ConsultQueryDTO> findAll();
}
