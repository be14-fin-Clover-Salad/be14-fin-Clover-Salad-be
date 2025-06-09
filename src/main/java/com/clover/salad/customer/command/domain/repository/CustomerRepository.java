package com.clover.salad.customer.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.customer.command.domain.aggregate.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByEmail(String email);
}
