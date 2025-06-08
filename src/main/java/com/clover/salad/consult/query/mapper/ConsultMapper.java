package com.clover.salad.consult.query.mapper;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConsultMapper {
        List<ConsultQueryDTO> findAll();

        ConsultQueryDTO findById(int id);

        List<ConsultQueryDTO> findAllActive();

        ConsultQueryDTO findActiveById(int id);

        List<ConsultQueryDTO> findByDepartmentName(String departmentName);

        ConsultQueryDTO findByDepartmentNameAndId(Map<String, Object> param);

        List<ConsultQueryDTO> findByEmployeeCode(String employeeCode);

        ConsultQueryDTO findByEmployeeCodeAndId(Map<String, Object> param);
}
