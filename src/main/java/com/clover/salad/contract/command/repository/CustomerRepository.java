package com.clover.salad.contract.command.repository;

import com.clover.salad.contract.command.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}