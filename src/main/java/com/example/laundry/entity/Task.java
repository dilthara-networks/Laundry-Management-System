// src/main/java/com/example/laundry/entity/Task.java
package com.example.laundry.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Order order;
    private String operator;
    private String status;
    private Date completionTime;
    private boolean qualityChecked;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCompletionTime() { return completionTime; }
    public void setCompletionTime(Date completionTime) { this.completionTime = completionTime; }
    public boolean isQualityChecked() { return qualityChecked; }
    public void setQualityChecked(boolean qualityChecked) { this.qualityChecked = qualityChecked; }
}