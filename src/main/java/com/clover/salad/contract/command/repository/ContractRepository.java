package com.clover.salad.contract.command.repository;

import com.clover.salad.contract.command.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {
}