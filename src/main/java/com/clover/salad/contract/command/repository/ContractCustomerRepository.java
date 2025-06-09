package com.clover.salad.contract.command.repository;

import com.clover.salad.customer.command.domain.aggregate.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractCustomerRepository extends JpaRepository<Customer, Integer> {
}