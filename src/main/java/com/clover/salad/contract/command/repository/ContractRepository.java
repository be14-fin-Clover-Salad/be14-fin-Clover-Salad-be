package com.clover.salad.contract.command.repository;

import com.clover.salad.contract.command.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {
}