// src/main/java/com/example/laundry/repository/TicketRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.Customer;
import com.example.laundry.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCustomer(Customer customer);
}