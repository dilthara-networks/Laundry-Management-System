// src/main/java/com/example/laundry/repository/FeedbackRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}