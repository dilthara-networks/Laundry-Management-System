// src/main/java/com/example/laundry/repository/OrderRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.Customer;
import com.example.laundry.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByDeliveryTypeAndStatus(String deliveryType, String status);
}