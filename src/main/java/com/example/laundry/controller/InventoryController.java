// src/main/java/com/example/laundry/controller/InventoryController.java
package com.example.laundry.controller;

import com.example.laundry.entity.InventoryItem;
import com.example.laundry.repository.InventoryItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InventoryController {
    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @GetMapping("/inventory/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!"inventory".equals(session.getAttribute("userType"))) {
            return "redirect:/staff/login";
        }
        model.addAttribute("items", inventoryItemRepository.findAll());
        return "inventory_dashboard";
    }

    @PostMapping("/inventory/add-item")
    public String addItem(@RequestParam String name, @RequestParam int quantity) {
        InventoryItem item = new InventoryItem();
        item.setName(name);
        item.setQuantity(quantity);
        item.setLowStock(quantity < 10); // Arbitrary threshold
        inventoryItemRepository.save(item);
        return "redirect:/inventory/dashboard";
    }

    @PostMapping("/inventory/update-item/{id}")
    public String updateItem(@PathVariable Long id, @RequestParam int quantity) {
        InventoryItem item = inventoryItemRepository.findById(id).orElse(null);
        if (item != null) {
            item.setQuantity(quantity);
            item.setLowStock(quantity < 10);
            inventoryItemRepository.save(item);
        }
        return "redirect:/inventory/dashboard";
    }
}