package com.clover.salad.contract.document.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.contract.document.entity.DocumentOrigin;

public interface DocumentOriginRepository extends JpaRepository<DocumentOrigin, Long> {
}
