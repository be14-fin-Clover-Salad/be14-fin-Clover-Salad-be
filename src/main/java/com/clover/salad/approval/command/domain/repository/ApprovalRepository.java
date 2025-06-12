package com.clover.salad.approval.command.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clover.salad.approval.command.domain.aggregate.entity.ApprovalEntity;
import com.clover.salad.approval.command.domain.aggregate.enums.ApprovalState;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Integer> {
	boolean existsByReqIdAndContractIdAndStateIn(int requesterId, int contractId, List<ApprovalState> requested);
}
