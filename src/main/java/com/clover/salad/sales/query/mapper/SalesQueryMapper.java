package com.clover.salad.sales.query.mapper;

import com.clover.salad.sales.query.dto.SalesQueryRequestDTO;
import com.clover.salad.sales.query.dto.SalesQueryResponseDTO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SalesQueryMapper {
	List<SalesQueryResponseDTO> selectSalesByCondition(SalesQueryRequestDTO query);
}
