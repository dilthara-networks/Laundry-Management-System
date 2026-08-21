// src/main/java/com/example/laundry/controller/AdminController.java
package com.example.laundry.controller;

import com.example.laundry.entity.Order;
import com.example.laundry.entity.ServiceEntity;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.repository.ServiceRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        return "admin_dashboard";
    }

    @GetMapping("/admin/set-services")
    public String setServicesPage(Model model, HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        model.addAttribute("services", serviceRepository.findAll());
        return "set_services";
    }

    @PostMapping("/admin/add-service")
    public String addService(@RequestParam String name, @RequestParam double price) {
        ServiceEntity service = new ServiceEntity();
        service.setName(name);
        service.setPrice(price);
        serviceRepository.save(service);
        return "redirect:/admin/set-services";
    }

    @GetMapping("/admin/update-service/{id}")
    public String updateServicePage(@PathVariable Long id, Model model, HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        ServiceEntity service = serviceRepository.findById(id).orElse(null);
        model.addAttribute("service", service);
        return "update_service";
    }

    @PostMapping("/admin/update-service")
    public String updateService(@RequestParam Long id, @RequestParam String name, @RequestParam double price) {
        ServiceEntity service = serviceRepository.findById(id).orElse(null);
        if (service != null) {
            service.setName(name);
            service.setPrice(price);
            serviceRepository.save(service);
        }
        return "redirect:/admin/set-services";
    }

    @PostMapping("/admin/delete-service/{id}")
    public String deleteService(@PathVariable Long id) {
        serviceRepository.deleteById(id);
        return "redirect:/admin/set-services";
    }

    @GetMapping("/admin/manage-orders")
    public String manageOrdersPage(Model model, HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        model.addAttribute("orders", orderRepository.findAll());
        return "manage_orders";
    }

    @PostMapping("/admin/accept-order/{id}")
    public String acceptOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus("accepted");
            orderRepository.save(order);
        }
        return "redirect:/admin/manage-orders";
    }

    @PostMapping("/admin/decline-order/{id}")
    public String declineOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus("declined");
            orderRepository.save(order);
        }
        return "redirect:/admin/manage-orders";
    }
}