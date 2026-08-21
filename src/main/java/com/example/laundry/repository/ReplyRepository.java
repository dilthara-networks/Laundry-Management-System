// src/main/java/com/example/laundry/repository/ReplyRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.Reply;
import com.example.laundry.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByTicket(Ticket ticket);
}