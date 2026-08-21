# 🧺 Laundry Management System

A full-stack web application for managing laundry service operations, built with **Java Spring Boot**, **Thymeleaf**, and **Microsoft SQL Server**. Developed as a group academic project to digitize and streamline a laundry business across multiple staff roles.

---

## 📌 Project Overview

This system provides a role-based management platform covering the complete order lifecycle — from customer registration and order placement, through washing and quality checking, to delivery and customer support.

---

## ✨ Features

### 👤 Customer Portal
- Register and log in securely (BCrypt password hashing)
- Browse available laundry services with pricing
- Place orders with **pickup** or **home delivery** options
- Simulated online payment flow
- Submit feedback and support tickets
- View order history and ticket replies

### 🛠️ Admin Panel
- Add, update, and delete laundry services
- View all customer orders
- Accept or decline incoming orders

### 📦 Supervisor Dashboard
- View accepted orders and assign them to operators
- Mark tasks as complete with timestamp
- Perform quality checks before dispatch

### 🚚 Delivery Coordinator Dashboard
- View orders ready for delivery
- Assign drivers and schedule delivery dates
- Update delivery status in real time

### 🗃️ Inventory Manager Dashboard
- Add and manage inventory items
- Automatic low-stock alerts (threshold: 10 units)

### 🎧 Customer Support Dashboard
- View and respond to customer support tickets
- View all customer feedback
- Manage and update ticket replies

---

## 🏗️ Tech Stack

| Layer        | Technology                        |
|--------------|-----------------------------------|
| Backend      | Java 21, Spring Boot 3.3.4        |
| Web Layer    | Spring MVC, Thymeleaf             |
| Database     | Microsoft SQL Server              |
| ORM          | Spring Data JPA / Hibernate       |
| Security     | BCrypt password hashing           |
| Frontend     | HTML5, CSS3                       |
| Build Tool   | Maven                             |

---

## 🗂️ Project Structure

```
src/
└── main/
    ├── java/com/example/laundry/
    │   ├── config/          # BCrypt + WebMVC configuration
    │   ├── controller/      # MVC Controllers (one per role)
    │   ├── entity/          # JPA Entity classes
    │   ├── repository/      # Spring Data JPA Repositories
    │   └── LaundryApplication.java
    └── resources/
        ├── templates/       # Thymeleaf HTML templates
        ├── static/
        │   ├── css/         # Stylesheets
        │   └── images/      # Background images and logo
        └── application.properties
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Microsoft SQL Server (running on `localhost:1433`)

### 1. Database Setup
Create a database named `LaundrySystem` in SQL Server. Tables are auto-created on first run (`ddl-auto=update`).

### 2. Configure Credentials
Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=LaundrySystem;encrypt=false;trustServerCertificate=true
spring.datasource.username=YOUR_SQL_USERNAME
spring.datasource.password=YOUR_SQL_PASSWORD
```

Or pass them as environment variables:
```bash
export DB_USERNAME=sa
export DB_PASSWORD=yourpassword
```

### 3. Run the Application
```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/laundry-management-system.git
cd laundry-management-system

# Build and run
./mvnw spring-boot:run
```

Visit **http://localhost:8080**

---

## 👥 User Roles & Login Credentials

### Customer
Register at `/customer/register` with your details.

### Staff (login at `/staff/login`)

| Role                  | Username     | Password       |
|-----------------------|--------------|----------------|
| Admin                 | Admin        | Admin123       |
| Inventory Manager     | InManager    | InManager123   |
| Supervisor            | Supervisor   | Supervisor123  |
| Customer Support      | CustSupport  | CustSupport123 |
| Delivery Coordinator  | DelCoord     | DelCoord123    |

---

## 🔄 Order Lifecycle

```
Customer Places Order
        ↓
Admin Accepts Order
        ↓
Supervisor Assigns Task to Operator
        ↓
Supervisor Marks Task Complete + Quality Check
        ↓
Delivery Coordinator Assigns Driver & Schedule
        ↓
Order Delivered ✓
```

---

## 🔐 Security Notes

- Customer passwords are hashed using **BCrypt** before storing
- Staff passwords are verified against **BCrypt hashes** — plain-text credentials are never stored
- Database credentials should be provided via environment variables, not hardcoded
- This is an academic project; a full production deployment would also include Spring Security filters and HTTPS

---

## 📄 License

This project was developed for academic purposes and is shared here as a portfolio reference.
