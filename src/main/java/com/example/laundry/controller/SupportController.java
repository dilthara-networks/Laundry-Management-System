// src/main/java/com/example/laundry/controller/SupportController.java
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SupportController {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SupportReplyRepository supportReplyRepository;

    @GetMapping("/support/dashboard")
    public String supportDashboard(Model model, HttpSession session) {
        if (!"support".equals(session.getAttribute("userType"))) {
            return "redirect:/support/login";
        }

        List<Ticket> tickets = ticketRepository.findAll();
        List<Feedback> feedbacks = feedbackRepository.findAll(); // NEW

        Map<Long, List<SupportReply>> repliesByTicket = new HashMap<>();
        for (Ticket t : tickets) {
            repliesByTicket.put(t.getId(), supportReplyRepository.findByTicketId(t.getId()));
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("feedbacks", feedbacks); // NEW
        model.addAttribute("repliesByTicket", repliesByTicket);

        return "support_dashboard";
    }

    @PostMapping("/support/reply-ticket/{id}")
    public String replyTicket(@PathVariable Long id, @RequestParam String content) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null) {
            Reply reply = new Reply();
            reply.setTicket(ticket);
            reply.setContent(content);
            replyRepository.save(reply);
        }
        return "redirect:/support/dashboard";
    }

    @PostMapping("/support/add-reply")
    public String addSupportReply(@RequestParam Long ticketId,
                                  @RequestParam String replyText,
                                  HttpSession session) {
        if (!"support".equals(session.getAttribute("userType"))) {
            return "redirect:/support/login";
        }

        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null) return "redirect:/support/dashboard";

        SupportReply reply = new SupportReply();
        reply.setTicket(ticket);
        reply.setReplyText(replyText);
        reply.setReplyDate(LocalDateTime.now());
        supportReplyRepository.save(reply);

        return "redirect:/support/dashboard";
    }

    @GetMapping("/support/update-reply/{id}")
    public String updateReplyPage(@PathVariable Long id, Model model, HttpSession session) {
        if (!"support".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        Reply reply = replyRepository.findById(id).orElse(null);
        model.addAttribute("reply", reply);
        return "update_reply";
    }

    @PostMapping("/support/update-reply")
    public String updateReply(@RequestParam Long id, @RequestParam String content) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply.setContent(content);
            replyRepository.save(reply);
        }
        return "redirect:/support/dashboard";
    }

    @PostMapping("/support/delete-reply/{id}")
    public String deleteReply(@PathVariable Long id) {
        replyRepository.deleteById(id);
        return "redirect:/support/dashboard";
    }

    @GetMapping("/support/customer-profile/{id}")
    public String customerProfile(@PathVariable Long id, Model model, HttpSession session) {
        if (!"support".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        Customer customer = customerRepository.findById(id).orElse(null);
        model.addAttribute("customer", customer);
        model.addAttribute("orders", orderRepository.findByCustomer(customer));
        return "customer_profile";
    }

    @GetMapping("/all-feedbacks")
    public String allFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackRepository.findAll());
        return "all_feedbacks";
    }
}