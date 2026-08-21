package com.example.laundry.repository;

import com.example.laundry.entity.SupportReply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupportReplyRepository extends JpaRepository<SupportReply, Long> {
    List<SupportReply> findByTicketId(Long ticketId);
}