// src/main/java/com/example/laundry/controller/CustomerController.java
package com.example.laundry.controller;

import com.example.laundry.entity.*;
import com.example.laundry.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ReplyRepository replyRepository;

    // Example: Customer Dashboard (apply same to others)
    @GetMapping("/customer/dashboard")
    public String customerDashboard(Model model, HttpSession session) {
        if (!"customer".equals(session.getAttribute("userType"))) {
            return "redirect:/customer/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/customer/login";
        }

        Customer customer = customerRepository.findById(userId).orElse(null);
        if (customer == null) {
            session.invalidate();
            return "redirect:/customer/login";
        }

        List<Order> orders = orderRepository.findByCustomer(customer);
        model.addAttribute("customer", customer);
        model.addAttribute("orders", orders != null ? orders : new ArrayList<>());
        return "customer_dashboard";
    }

    @GetMapping("/customer/create-order")
    public String createOrderPage(Model model, HttpSession session) {
        if (!"customer".equals(session.getAttribute("userType"))) {
            return "redirect:/customer/login";
        }
        model.addAttribute("services", serviceRepository.findAll());
        return "create_order";
    }

    @PostMapping("/customer/create-order")
    public String createOrder(@RequestParam Long serviceId, @RequestParam String deliveryType,
                              @RequestParam(required = false) String address, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Customer customer = customerRepository.findById(userId).orElse(null);
        ServiceEntity service = serviceRepository.findById(serviceId).orElse(null);
        Order order = new Order();
        order.setCustomer(customer);
        order.setServiceEntity(service);
        order.setDeliveryType(deliveryType);
        if ("delivery".equals(deliveryType)) {
            order.setAddress(address);
        }
        order.setStatus("pending");
        orderRepository.save(order);
        return "redirect:/customer/pay/" + order.getId();
    }

    @GetMapping("/customer/pay/{orderId}")
    public String payPage(@PathVariable Long orderId, Model model, HttpSession session) {
        if (!"customer".equals(session.getAttribute("userType"))) {
            return "redirect:/customer/login";
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        model.addAttribute("order", order);
        return "payment";
    }

    @PostMapping("/customer/pay")
    public String pay(@RequestParam Long orderId, @RequestParam String cardNumber, @RequestParam String expiry, @RequestParam String cvv) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus("paid"); // Fake payment
            orderRepository.save(order);
        }
        return "redirect:/customer/dashboard";
    }

    @GetMapping("/customer/submit-feedback")
    public String feedbackPage(HttpSession session) {
        if (!"customer".equals(session.getAttribute("userType"))) {
            return "redirect:/customer/login";
        }
        return "submit_feedback";
    }

    @PostMapping("/customer/submit-feedback")
    public String submitFeedback(@RequestParam String content, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Customer customer = customerRepository.findById(userId).orElse(null);
        Feedback feedback = new Feedback();
        feedback.setCustomer(customer);
        feedback.setContent(content);
        feedbackRepository.save(feedback);
        return "redirect:/customer/dashboard";
    }

    @Autowired
    private SupportReplyRepository supportReplyRepository;

    @GetMapping("/customer/submit-ticket")
    public String showSubmitTicket(Model model, HttpSession session) {
        if (!"customer".equals(session.getAttribute("userType"))) {
            return "redirect:/customer/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        Customer customer = customerRepository.findById(userId).orElse(null);
        if (customer == null) return "redirect:/customer/login";

        List<Ticket> tickets = ticketRepository.findByCustomer(customer);

        // Build replies map
        Map<Long, List<SupportReply>> repliesByTicket = new HashMap<>();
        for (Ticket ticket : tickets) {
            List<SupportReply> replies = supportReplyRepository.findByTicketId(ticket.getId());
            repliesByTicket.put(ticket.getId(), replies != null ? replies : new ArrayList<>());
        }

        model.addAttribute("customer", customer);
        model.addAttribute("tickets", tickets);
        model.addAttribute("repliesByTicket", repliesByTicket);

        return "submit_ticket";
    }

    @PostMapping("/customer/add-reply")
    public String addReply(@RequestParam Long ticketId,
                           @RequestParam String replyText,
                           HttpSession session) {
        if (!"customer".equals(session.getAttribute("userType"))) {
            return "redirect:/customer/login";
        }

        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null) return "redirect:/customer/submit-ticket";

        SupportReply reply = new SupportReply();
        reply.setTicket(ticket);
        reply.setReplyText(replyText);
        reply.setReplyDate(LocalDateTime.now());
        supportReplyRepository.save(reply);

        return "redirect:/customer/submit-ticket";
    }

    @PostMapping("/customer/submit-ticket")
    public String submitTicket(@RequestParam String subject,
                               @RequestParam String message,
                               HttpSession session) {
        if (!"customer".equals(session.getAttribute("userType"))) {
            return "redirect:/customer/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        Customer customer = customerRepository.findById(userId).orElse(null);
        if (customer == null) return "redirect:/customer/login";

        Ticket ticket = new Ticket();
        ticket.setSubject(subject);
        ticket.setMessage(message);
        ticket.setCustomer(customer);
        ticket.setStatus("OPEN");
        ticketRepository.save(ticket);

        return "redirect:/customer/submit-ticket";
    }
}