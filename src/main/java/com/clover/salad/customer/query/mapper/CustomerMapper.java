package com.clover.salad.customer.query.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clover.salad.customer.query.dto.CustomerQueryDTO;

@Mapper
public interface CustomerMapper {

    List<CustomerQueryDTO> findAll(); // 관리자용 전체 조회(삭제 포함)

    List<CustomerQueryDTO> findAllActive(); // is_deleted = false

    CustomerQueryDTO findCustomerById(@Param("id") int id); // 수정 필요

    /* 25. 06. 12 성연님 요청 사항 */
    Integer findRegisteredCustomerId(@Param("customerName") String customerName,
            @Param("customerBirthdate") String customerBirthdate,
            @Param("customerPhone") String customerPhone);
}
