package com.clover.salad.contract.command.repository;

import com.clover.salad.contract.command.entity.ContractFileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ContractFileHistoryRepository extends JpaRepository<ContractFileHistory, Integer> {

	List<ContractFileHistory> findByContract_Id(int contractId); // 연관관계 기반 접근

	int countByContract_Id(int contractId);

	@Query("SELECT MAX(h.version) FROM ContractFileHistory h WHERE h.contract.id = :contractId")
	Optional<Integer> findMaxVersionByContractId(@Param("contractId") int contractId);
}
