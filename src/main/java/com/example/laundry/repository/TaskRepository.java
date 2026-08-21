// src/main/java/com/example/laundry/repository/TaskRepository.java
package com.example.laundry.repository;

import com.example.laundry.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}