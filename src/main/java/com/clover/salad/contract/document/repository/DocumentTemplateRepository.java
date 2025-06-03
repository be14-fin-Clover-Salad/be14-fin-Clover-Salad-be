package com.clover.salad.contract.document.repository;

import com.clover.salad.contract.document.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Integer> {
}
