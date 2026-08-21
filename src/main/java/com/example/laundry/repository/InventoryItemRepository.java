// src/main/java/com/example/laundry/repository/InventoryItemRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
}