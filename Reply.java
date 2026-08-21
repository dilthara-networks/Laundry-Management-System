// src/main/java/com/example/laundry/entity/Reply.java
package com.example.laundry.entity;

import jakarta.persistence.*;

@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Ticket ticket;
    private String content;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}