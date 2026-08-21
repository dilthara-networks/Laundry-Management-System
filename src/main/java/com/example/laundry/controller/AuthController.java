package com.example.laundry.controller;

import com.example.laundry.entity.Customer;
import com.example.laundry.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    // ─── Customer Auth ────────────────────────────────────────────────────────

    @GetMapping("/customer/login")
    public String customerLoginPage() {
        return "customer_login";
    }

    @GetMapping("/customer/register")
    public String customerRegisterPage() {
        return "customer_register";
    }

    @PostMapping("/customer/register")
    public String registerCustomer(@RequestParam String firstName,
                                   @RequestParam String lastName,
                                   @RequestParam String nic,
                                   @RequestParam String phone,
                                   @RequestParam String address,
                                   @RequestParam String email,
                                   @RequestParam String password) {
        // Prevent duplicate email registration
        if (customerRepository.findByEmail(email) != null) {
            return "redirect:/customer/register?error=emailExists";
        }

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setNic(nic);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setEmail(email);
        // Hash the password before saving
        customer.setPassword(passwordEncoder.encode(password));
        customerRepository.save(customer);
        return "redirect:/customer/login?registered=true";
    }

    @PostMapping("/customer/login")
    public String loginCustomer(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session) {
        Customer customer = customerRepository.findByEmail(email);
        // Use BCrypt to verify the hashed password
        if (customer != null && passwordEncoder.matches(password, customer.getPassword())) {
            session.setAttribute("userType", "customer");
            session.setAttribute("userId", customer.getId());
            return "redirect:/customer/dashboard";
        }
        return "redirect:/customer/login?error=true";
    }

    // ─── Staff Auth ───────────────────────────────────────────────────────────

    @GetMapping("/staff/login")
    public String staffLoginPage() {
        return "staff_login";
    }

    @PostMapping("/staff/login")
    public String loginStaff(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session) {
        // Staff credentials are stored as BCrypt hashes
        // Plain-text equivalents for reference (do not store these):
        //   Admin / Admin123
        //   InManager / InManager123
        //   Supervisor / Supervisor123
        //   CustSupport / CustSupport123
        //   DelCoord / DelCoord123
        StaffRole role = StaffRole.fromCredentials(username, password, passwordEncoder);
        if (role == null) {
            return "redirect:/staff/login?error=true";
        }
        session.setAttribute("userType", role.sessionValue);
        return "redirect:" + role.dashboardPath;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ─── Staff Credentials (BCrypt-hashed) ───────────────────────────────────

    private enum StaffRole {
        ADMIN("admin", "/admin/dashboard",
                "$2a$12$6T7oN2D3emkJBYjUVGl.oeA7o4eoIxnpLb.YJRBiJ1Rk/hqCQPe8y"),       // Admin123
        INVENTORY("inventory", "/inventory/dashboard",
                "$2a$12$Cd3rF3HvJroBh/QjqrFLuOUiMwqSe2M8BkYvWXNYxH.vdPXf3Mxlu"),      // InManager123
        SUPERVISOR("supervisor", "/supervisor/dashboard",
                "$2a$12$jj7ZuRxOEmkAIU2UbHHHRuMvg5ZF6GCY4RhEmfr9jUiFiw8Wa/P5O"),      // Supervisor123
        SUPPORT("support", "/support/dashboard",
                "$2a$12$5FiqFY9pBxJVABvBzP4B2u1rPjEEO0JTQlOBUU9m9S5WUBuOhYGlG"),      // CustSupport123
        DELIVERY("delivery", "/delivery/dashboard",
                "$2a$12$wXF2wFjVzOr2rJO1Bb8JMOgKYRVsME4j6bM3Xa2YUDaNqDUfLV3Rm");      // DelCoord123

        final String sessionValue;
        final String dashboardPath;
        final String hashedPassword;

        StaffRole(String sessionValue, String dashboardPath, String hashedPassword) {
            this.sessionValue = sessionValue;
            this.dashboardPath = dashboardPath;
            this.hashedPassword = hashedPassword;
        }

        static StaffRole fromCredentials(String username, String password, BCryptPasswordEncoder encoder) {
            return switch (username) {
                case "Admin"       -> encoder.matches(password, ADMIN.hashedPassword)       ? ADMIN       : null;
                case "InManager"   -> encoder.matches(password, INVENTORY.hashedPassword)   ? INVENTORY   : null;
                case "Supervisor"  -> encoder.matches(password, SUPERVISOR.hashedPassword)  ? SUPERVISOR  : null;
                case "CustSupport" -> encoder.matches(password, SUPPORT.hashedPassword)     ? SUPPORT     : null;
                case "DelCoord"    -> encoder.matches(password, DELIVERY.hashedPassword)    ? DELIVERY    : null;
                default -> null;
            };
        }
    }
}
