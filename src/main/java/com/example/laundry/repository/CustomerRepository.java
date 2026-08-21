// src/main/java/com/example/laundry/repository/CustomerRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}