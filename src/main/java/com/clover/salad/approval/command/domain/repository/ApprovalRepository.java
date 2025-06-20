package com.clover.salad.approval.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clover.salad.approval.command.domain.aggregate.entity.ApprovalEntity;


public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Integer> {
}
