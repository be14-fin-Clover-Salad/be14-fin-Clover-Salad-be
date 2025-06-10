package com.clover.salad.sales.command.application.mapper;

import com.clover.salad.sales.command.application.dto.SalesCommandDTO;
import com.clover.salad.sales.command.domain.aggregate.entity.SalesEntity;

public class SalesMapper {

	/* 설명.
	 *  실수로 유틸리티 클래스가 인스턴트가 되는 것을 방지하기 위한 제약
	* */
	private SalesMapper() {}

	public static SalesEntity toEntity(SalesCommandDTO salesCommandDTO) {
		return SalesEntity.builder()
			.salesDate(salesCommandDTO.getSalesDate())
			.department(salesCommandDTO.getDepartment())
			.employeeName(salesCommandDTO.getEmployeeName())
			.amount(salesCommandDTO.getAmount())
			.contractId(salesCommandDTO.getContractId())
			.isDeleted(false)
			.build();
	}
}
