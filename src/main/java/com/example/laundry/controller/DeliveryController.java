package com.example.laundry.controller;

import com.example.laundry.entity.Delivery;
import com.example.laundry.entity.Order;
import com.example.laundry.repository.DeliveryRepository;
import com.example.laundry.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class DeliveryController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @GetMapping("/delivery/dashboard")
    public String deliveryDashboard(Model model, HttpSession session) {
        if (!"delivery".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        model.addAttribute("orders", orderRepository.findByDeliveryTypeAndStatus("delivery", "ready"));
        model.addAttribute("deliveries", deliveryRepository.findAll());
        return "delivery_dashboard";
    }

    @PostMapping("/delivery/assign")
    public String assignDelivery(@RequestParam Long orderId,
                                 @RequestParam String driver,
                                 @RequestParam("schedule") LocalDate schedule,
                                 HttpSession session) {
        if (!"delivery".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            Delivery delivery = new Delivery();
            delivery.setOrder(order);
            delivery.setDriver(driver);
            delivery.setStatus("assigned");
            delivery.setSchedule(schedule);
            deliveryRepository.save(delivery);

            order.setStatus("out_for_delivery");
            orderRepository.save(order);
        }
        return "redirect:/delivery/dashboard";
    }

    @PostMapping("/delivery/update-status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        Delivery delivery = deliveryRepository.findById(id).orElse(null);
        if (delivery != null) {
            delivery.setStatus(status);
            deliveryRepository.save(delivery);
        }
        return "redirect:/delivery/dashboard";
    }
}
