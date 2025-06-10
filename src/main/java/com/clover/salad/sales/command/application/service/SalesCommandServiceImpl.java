package com.clover.salad.sales.command.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.clover.salad.sales.command.application.dto.SalesCommandDTO;
import com.clover.salad.sales.command.application.mapper.SalesMapper;
import com.clover.salad.sales.command.domain.aggregate.entity.SalesEntity;
import com.clover.salad.sales.command.domain.repository.SalesRepository;
import com.clover.salad.security.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SalesCommandServiceImpl implements SalesCommandService {

	private final SalesRepository salesRepository;

	@Autowired
	public SalesCommandServiceImpl(SalesRepository salesRepository) {
		this.salesRepository = salesRepository;
	}

	@Override
	public int createSales(SalesCommandDTO salesCommandDTO) {
		SalesEntity salesEntity = SalesMapper.toEntity(salesCommandDTO);
		return salesRepository.save(salesEntity).getId();
	}

	@Override
	public void deleteSales(Integer id) {


		SalesEntity sales = salesRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 매출입니다."));

		sales.delete();
		salesRepository.save(sales);
	}
}
