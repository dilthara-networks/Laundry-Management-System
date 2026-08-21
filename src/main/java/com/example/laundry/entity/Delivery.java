// src/main/java/com/example/laundry/entity/Delivery.java
package com.example.laundry.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    private String driver;
    private String status;

    @Column
    private LocalDate schedule;

    // GETTERS & SETTERS — FIXED
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getSchedule() { return schedule; }
    public void setSchedule(LocalDate schedule) { this.schedule = schedule; }  // FIXED
}