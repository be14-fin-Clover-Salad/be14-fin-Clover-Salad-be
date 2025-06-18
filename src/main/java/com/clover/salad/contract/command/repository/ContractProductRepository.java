package com.clover.salad.contract.command.repository;

import java.util.List;

import com.clover.salad.contract.command.entity.ContractProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractProductRepository extends JpaRepository<ContractProductEntity, Integer> {
	List<ContractProductEntity> findByContractId(int contractId);
}
