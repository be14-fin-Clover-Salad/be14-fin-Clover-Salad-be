package com.clover.salad.contract.command.repository;

import com.clover.salad.contract.command.entity.ContractFileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractFileHistoryRepository extends JpaRepository<ContractFileHistory, Integer> {

	Optional<ContractFileHistory> findByContract_Id(int contractId);

	Optional<ContractFileHistory> findByReplacedContract_Id(int replacedContractId);

	@Query("SELECT MAX(h.version) FROM ContractFileHistory h WHERE h.replacedContract.id = :rootContractId")
	Optional<Integer> findMaxVersionByReplacedContractId(int rootContractId);
}
