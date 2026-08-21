// src/main/java/com/example/laundry/repository/DeliveryRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}