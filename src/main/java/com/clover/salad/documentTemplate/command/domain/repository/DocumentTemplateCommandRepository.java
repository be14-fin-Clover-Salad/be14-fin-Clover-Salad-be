package com.clover.salad.documentTemplate.command.domain.repository;

import com.clover.salad.contract.document.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTemplateCommandRepository extends JpaRepository<DocumentTemplate, Integer> {
}
