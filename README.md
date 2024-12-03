
# RBAC Authentication & Authorization

## Overview

This project demonstrates Role-Based Access Control (RBAC), authentication, and authorization in a backend server. It showcases how requests are handled in a Spring Boot application, traversing through filter chains, ensuring validity, and verifying roles before granting access to the controller layer.

### Key Features:
- **Endpoint Security**: Easily secure endpoints, exposing only registration-related endpoints to unauthenticated users.
- **Email Confirmation**: Users must confirm their email address via a verification email before logging in.
- **Global Exception Handling**: All exceptions are managed centrally using a global controller advisor.
- **RBAC Implementation**: Role-based access to endpoints (e.g., Admin, Super Admin).

---

## Setup

### Prerequisites
Ensure the following are installed on your system:
- **Java 21**
- **MySQL Database**

### Configuration
Update the `application.properties` file with your database details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Replace `your_database_name`, `your_username`, and `your_password` with your MySQL database details.

```properties
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_PASSWORD
```

Replace `YOUR_EMAIL`,and `YOUR_PASSWORD` with your SMTP details.

---

## Running the Project

1. Clone the project repository.
2. Open the project in your preferred IDE.
3. Ensure all setup configurations are complete.
4. Run the project from `VrcSecurityAssignmentApplication.class`.

---

## Endpoints

### 1. **User Signup**
**POST**: `http://localhost:8005/auth/signup`  
**Parameters**:  
```json
{
  "email": "example@example.com",
  "password": "your_password",
  "fullName": "Your Full Name"
}
```

### 2. **User Login**
**POST**: `http://localhost:8005/auth/login`  
**Parameters**:  
```json
{
  "email": "example@example.com",
  "password": "your_password"
}
```

### 3. **Add Admin (Restricted)**
**POST**: `http://localhost:8005/admins`  
**Parameters**:  
```json
{
  "email": "example@example.com",
  "password": "your_password",
  "fullName": "Admin Full Name"
}
```
**Authentication**: Required  
**Authorization**: Only a SUPER ADMIN can add admins. SUPERADMIN details: email: admin@gmail.com, password: 123456

### 4. **Get All Users**
**GET**: `http://localhost:8005/users`  
**Parameters**: None  
**Authentication**: Required  
**Authorization**: Requires ADMIN or SUPER ADMIN role.

### 5. **Get Current User Information**
**GET**: `http://localhost:8005/users/me`  
**Parameters**: None  
**Authentication**: Required  
**Authorization**: Any authenticated user can access this endpoint.

---

## Technologies Used

- **Spring Boot**: Backend framework
- **Spring Security**: For authentication and authorization
- **Hibernate**: ORM for database interaction
- **JWT Tokens**: Stateless authentication
- **SMTP Mail**: For email confirmation

---

## License
This project is for demonstration purposes and can be used as a reference for implementing RBAC and secure backend services.

---
