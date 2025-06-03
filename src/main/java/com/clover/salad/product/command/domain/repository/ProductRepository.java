package com.clover.salad.product.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clover.salad.product.command.domain.aggregate.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
