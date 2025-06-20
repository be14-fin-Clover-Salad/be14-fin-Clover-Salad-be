package com.clover.salad.customer.command.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clover.salad.customer.command.domain.aggregate.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
        boolean existsByEmail(String email);

        Optional<Customer> findByName(String customerName);

        // name, phone은 필수 중 하나, birthdate는 optional
        @Query("SELECT c FROM Customer c WHERE " + "(:name IS NULL OR c.name = :name) AND "
                        + "(:phone IS NULL OR c.phone = :phone) AND "
                        + "(:birthdate IS NULL OR c.birthdate = :birthdate)")
        Optional<Customer> findByNameAndPhoneAndBirthdateOptional(String name, String phone,
                        String birthdate);

        // 고성연 Transcational 안에서 도중 사용할 jpa 조회용
        Optional<Customer> findTopByNameAndBirthdateAndPhoneOrderByRegisterAtDesc(String name,
                        String birthdate, String phone);

}
