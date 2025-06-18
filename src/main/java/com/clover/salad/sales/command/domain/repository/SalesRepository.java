package com.clover.salad.sales.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.sales.command.domain.aggregate.entity.SalesEntity;

public interface SalesRepository extends JpaRepository<SalesEntity, Integer> {

}
