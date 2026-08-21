package com.example.laundry.entity;

import com.example.laundry.entity.Customer;
import com.example.laundry.entity.ServiceEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "laundry_order")  // ← FIXED
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String deliveryType;
    private String status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_entity_id")
    private ServiceEntity serviceEntity;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public ServiceEntity getServiceEntity() { return serviceEntity; }
    public void setServiceEntity(ServiceEntity serviceEntity) { this.serviceEntity = serviceEntity; }
}