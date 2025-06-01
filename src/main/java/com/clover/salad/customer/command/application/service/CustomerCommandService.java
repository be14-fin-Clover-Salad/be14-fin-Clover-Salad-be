package com.clover.salad.customer.command.application.service;

import com.clover.salad.customer.command.application.dto.CustomerDTO;

public interface CustomerCommandService {
	void registerCustomer(CustomerDTO CustomerDTO);

	void updateCustomer(int customerId, CustomerDTO customerDTO);

	void deleteCustomer(int customerId);
}
