package com.clover.salad.customer.command.application.service;

import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.dto.CustomerUpdateRequest;

public interface CustomerCommandService {
	void registerCustomer(CustomerCreateRequest request);

	void updateCustomer(int customerId, CustomerUpdateRequest request);

	// void deleteCustomer(int id);
}
