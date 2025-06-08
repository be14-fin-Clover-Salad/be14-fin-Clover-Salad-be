package com.clover.salad.contract.command.repository;

import com.clover.salad.contract.command.entity.ContractProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractProductRepository extends JpaRepository<ContractProductEntity, Integer> {
}
