package com.clover.salad.customer.command.application.service;

import com.clover.salad.customer.command.application.dto.CustomerCreateRequest;
import com.clover.salad.customer.command.application.dto.CustomerUpdateRequest;

public interface CustomerCommandService {
	void registerCustomer(CustomerCreateRequest request);

	Integer registerCustomer(CustomerCreateRequest request, boolean bypassValidation);

	void updateCustomer(int customerId, CustomerUpdateRequest request);

	// void deleteCustomer(int id);

	void updateCustomer(int customerId, CustomerUpdateRequest request, boolean bypassValidation);
}
