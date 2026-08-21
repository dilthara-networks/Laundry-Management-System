// src/main/java/com/example/laundry/controller/SupervisorController.java
package com.example.laundry.controller;

import com.example.laundry.entity.Order;
import com.example.laundry.entity.Task;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class SupervisorController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TaskRepository taskRepository;

    // Supervisor Dashboard
    @GetMapping("/supervisor/dashboard")
    public String supervisorDashboard(Model model, HttpSession session) {
        if (!"supervisor".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        model.addAttribute("orders", orderRepository.findAll().stream()
                .filter(o -> "accepted".equals(o.getStatus())).toList());
        model.addAttribute("tasks", taskRepository.findAll());
        return "supervisor_dashboard";
    }

    @PostMapping("/supervisor/assign-task")
    public String assignTask(@RequestParam Long orderId, @RequestParam String operator) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            Task task = new Task();
            task.setOrder(order);
            task.setOperator(operator);
            task.setStatus("assigned");
            taskRepository.save(task);
            order.setStatus("in_progress");
            orderRepository.save(order);
        }
        return "redirect:/supervisor/dashboard";
    }

    @PostMapping("/supervisor/complete-task/{id}")
    public String completeTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setStatus("completed");
            task.setCompletionTime(new Date());
            taskRepository.save(task);
            // Assume no delay alert logic for simplicity
            Order order = task.getOrder();
            order.setStatus("ready");
            orderRepository.save(order);
        }
        return "redirect:/supervisor/dashboard";
    }

    @PostMapping("/supervisor/check-quality/{id}")
    public String checkQuality(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setQualityChecked(true);
            taskRepository.save(task);
        }
        return "redirect:/supervisor/dashboard";
    }
}