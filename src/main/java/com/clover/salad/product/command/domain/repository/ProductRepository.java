package com.clover.salad.product.command.domain.repository;

import com.clover.salad.product.command.domain.aggregate.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	Optional<Product> findByNameAndSerialNumber(String name, String serialNumber);
}
