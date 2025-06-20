package com.clover.salad.consult.command.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.consult.command.domain.aggregate.entity.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    Optional<Consultation> findByIdAndIsDeletedFalse(int consultId);
}
