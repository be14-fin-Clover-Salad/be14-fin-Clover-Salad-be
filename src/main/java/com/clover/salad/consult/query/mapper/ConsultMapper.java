package com.clover.salad.consult.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;

@Mapper
public interface ConsultMapper {

    ConsultQueryDTO findById(int id);

    List<ConsultQueryDTO> findAll(@Param("limit") int limit, @Param("offset") int offset);

}
