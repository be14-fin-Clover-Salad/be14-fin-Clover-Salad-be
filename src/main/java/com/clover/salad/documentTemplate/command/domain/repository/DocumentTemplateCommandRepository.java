package com.clover.salad.documentTemplate.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.contract.document.entity.DocumentTemplate;

@Repository
public interface DocumentTemplateCommandRepository extends JpaRepository<DocumentTemplate, Integer> {
}
