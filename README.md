# ✅ Task Management App

> A backend system for managing tasks, projects, comments, file attachments, and notifications. Think of it as a lightweight mix of Trello, Jira, and Telegram — all powered by Spring Boot. 🚀

---

## 🔍 Overview

This is a pet project built with **Spring Boot** to help teams manage:
- users, projects, tasks, comments;
- task assignments and notifications;
- file uploads via **Dropbox API**;
- real-time alerts via **Telegram**;
- protected routes via **JWT + Spring Security**;
- API documentation via **Swagger**;
- and deployment via **Docker + CI/CD**.

---

## 🛠️ Technologies and Tools

| Technology            | Purpose                                        |
| --------------------- | ---------------------------------------------- |
| **Java 17**           | Main programming language                      |
| **Spring Boot**       | Framework for building REST APIs               |
| **Spring Security**   | Authentication, authorization, and roles       |
| **Spring Data JPA**   | Database access via ORM                        |
| **Hibernate**         | JPA provider for MySQL interaction             |
| **Spring Web**        | HTTP request handling                          |
| **Spring Test**       | Integration testing                            |
| **JUnit**             | Unit testing                                   |
| **Mockito**           | Mock objects for testing                       |
| **TestContainers**    | Tests with isolated containers                 |
| **Liquibase**         | Database migrations                            |
| **MapStruct**         | Automatic DTO ↔ Entity mapping                 |
| **Lombok**            | Generates getters, setters, constructors, etc. |
| **JWT Token**         | Secure token-based authentication              |
| **MySQL**             | Relational database                            |
| **Maven**             | Dependency management and build tool           |
| **Docker**            | Application containerization                   |
| **Swagger / OpenAPI** | REST API documentation                         |
| **Postman**           | API testing and demonstration                  |

---

## 🚀 Getting Started Locally

1. **Clone the repository**:
```bash
git clone https://github.com/your-username/task-management-app.git
cd task-management-app
````

2. **Create `.env` file**:

```bash
cp .env.sample .env
```

Fill in environment variables for DB, JWT, Telegram, and Dropbox.

3. **Run using Docker**:

```bash
docker-compose up --build
```

4. **Access Swagger UI**:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Authorization

The app uses **JWT tokens** for secure access.

Include the token in the `Authorization` header for protected endpoints:

```
Authorization: Bearer <your_token>
```

---

## 📁 Project Structure

```
├── annotation       # Custom anotations
├── mapper           # Mappers
├── security         # JWT utils and logic
├── validation       # Validation logic
├── controller       # REST API controllers
├── dto              # Request/Response DTOs
├── model            # JPA entities (User, Task, Project, etc.)
├── repository       # Spring Data JPA Repositories
├── service          # Business logic
├── config           # Security, Swagger, Telegram config
├── exception        # Custom exceptions and handlers
└── util             # Utility classes (Mappers, Validators, etc.)
```

---

## 📌 Available Controllers

### 🔐 Auth Controller

Handles authentication and registration:

* `POST /api/auth/register` – Register new user
* `POST /api/auth/login` – Authenticate user and return JWT

---

### 👤 Users Controller

Manage user profile and roles:

* `PUT /api/users/{id}/role` – Update user role (ADMIN only)
* `GET /api/users/me` – Retrieve current user profile
* `PUT /api/users/me` – Update profile
* `PATCH /api/users/me` – Partially update profile

---

### 📁 Project Controller

CRUD operations for managing projects:

* `POST /api/projects` – Create a new project
* `GET /api/projects` – Retrieve all user's projects
* `GET /api/projects/{id}` – Retrieve project by ID
* `PUT /api/projects/{id}` – Update a project
* `DELETE /api/projects/{id}` – Delete a project

---

### ✅ Task Controller

CRUD operations for managing tasks:

* `POST /api/tasks` – Create a new task
* `GET /api/tasks` – Get tasks (can filter by project)
* `GET /api/tasks/{id}` – Get task by ID
* `PUT /api/tasks/{id}` – Update a task
* `DELETE /api/tasks/{id}` – Delete a task

---

### 💬 Comment Controller

Add and view comments on tasks:

* `POST /api/comments` – Add a comment to a task
* `GET /api/comments?taskId={taskId}` – Get comments for a task

---

### 📎 Attachment Controller

Handles file uploads to Dropbox:

* `POST /api/attachments` – Upload a file to Dropbox (linked to a task)
* `GET /api/attachments?taskId={taskId}` – List attachments for a task

*Note: Files are stored in Dropbox, and only the file ID is saved in the database.*

---

### 🏷️ Label Controller

Manage labels and assign to tasks:

* `POST /api/labels` – Create a new label
* `GET /api/labels` – List all labels
* `PUT /api/labels/{id}` – Update a label
* `DELETE /api/labels/{id}` – Delete a label

---
### 🔔 Telegram Controller
* `GET /api/telegram/info` – get info about connecting telegram notification

---

## 🧪 Testing

* Unit tests using **JUnit 5** and **Mockito**
* Integration tests using H2/Testcontainers
* `TestUtil` class for test data creation

---

## 📞 Contacts

* **Author:** Mykola Turak
* **GitHub:** https://github.com/MykolaTurak
* **LinkedIn:** https://www.linkedin.com/in/mykola-turak-1b6853312/
---