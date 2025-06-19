package com.clover.salad.customer.command.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clover.salad.customer.command.domain.aggregate.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByEmail(String email);

    // 고성연 Transcational 안에서 도중 사용할 jpa 조회용
    Optional<Customer> findTopByNameAndBirthdateAndPhoneOrderByRegisterAtDesc(
        String name, String birthdate, String phone
    );

}
