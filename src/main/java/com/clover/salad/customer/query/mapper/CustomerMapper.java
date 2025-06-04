package com.clover.salad.customer.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.customer.query.dto.CustomerQueryDTO;

@Mapper
public interface CustomerMapper {
    CustomerQueryDTO findCustomerById(@Param("id") int id);

    List<CustomerQueryDTO> findAll(); // 전체 조회 (삭제 포함)

    List<CustomerQueryDTO> findAllActive(); // is_deleted = false
}
