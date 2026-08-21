// src/main/java/com/example/laundry/repository/ServiceRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}