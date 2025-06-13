package com.clover.salad.qna.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.qna.command.domain.aggregate.entity.Qna;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Integer> {
}
