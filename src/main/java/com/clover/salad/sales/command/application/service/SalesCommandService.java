package com.clover.salad.sales.command.application.service;

import com.clover.salad.sales.command.application.dto.SalesCommandDTO;

public interface SalesCommandService {
	int createSales(SalesCommandDTO salesCommandDTO);

	void deleteSales(Integer id);
}
