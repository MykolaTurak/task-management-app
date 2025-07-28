# ✅ Task Management App

**Task Management App** is a **pet project** designed to help teams manage tasks and projects. It combines features for managing projects, tasks, comments, files, and notifications — somewhat like a lightweight version of Trello, Jira, and Telegram all in one, built with **Spring Boot**.

---

## 🚀 Key Features

* Full CRUD for users, projects, tasks, comments, labels, and attachments.
* Roles and security — protected with JWT and Spring Security.
* Dropbox integration for file storage.
* Notifications via Telegram.
* Automated database migrations via Liquibase.
* API documentation in Swagger UI.
* Containerization via Docker and CI/CD.

---

## 🛠 Technologies

| Technology            | Purpose                                        |
| --------------------- | ---------------------------------------------- |
| **Java 17**           | Main programming language                      |
| **Spring Boot**       | Framework for building REST APIs               |
| **Spring Security**   | Authentication, authorization, and roles       |
| **Spring Data JPA**   | ORM for database interaction                   |
| **Hibernate**         | JPA provider for MySQL                         |
| **Spring Web**        | HTTP request handling                          |
| **JUnit 5**           | Unit testing                                   |
| **Mockito**           | Mocking for tests                              |
| **TestContainers**    | Containerized testing                          |
| **Liquibase**         | Database migrations                            |
| **MapStruct**         | Automatic DTO ↔ Entity mapping                 |
| **Lombok**            | Generates getters, setters, constructors, etc. |
| **JWT Token**         | Token-based authentication                     |
| **MySQL**             | Relational database                            |
| **Maven**             | Dependency management and build tool           |
| **Docker**            | Application containerization                   |
| **Swagger / OpenAPI** | REST API documentation                         |
| **Postman**           | API testing                                    |

---

## 🚀 Running Locally

1. Clone the repository:

```bash
git clone https://github.com/MykolaTurak/task-management-app
cd task-management-app
```

2. Create a `.env` file:

```bash
cp .env.sample .env
```

3. Configure environment variables in the `.env` file:

```env
# === Dropbox ===
DROPBOX_TOKEN=your-dropbox-access-token

# === Telegram Bot ===
TELEGRAM_BOT_TOKEN=your-telegram-bot-token
TELEGRAM_BOT_USERNAME=your-telegram-bot-username

# === MySQL Database ===
MYSQL_ROOT_PASSWORD=your-mysql-root-password
MYSQL_DATABASE=your-database-name
MYSQL_USER=your-database-user
MYSQL_PASSWORD=your-database-password

# === MySQL Ports ===
MYSQL_LOCAL_PORT=3307
MYSQL_DOCKER_PORT=3306

# === Spring Boot App Ports ===
SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8081

# === Debugging ===
DEBUG_PORT=5005
```

> ⚠️ Replace all `your-...` values with your actual credentials, passwords, and tokens.
> Keep your `.env` file secure and do not commit it to public repositories.

4. Start the app using Docker Compose:

```bash
docker-compose up --build
```

5. Open Swagger UI to explore and test the API:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Authorization

The app uses **JWT** for security.
Include the token in the request header:

```
Authorization: Bearer <your_token>
```

---

## 📁 Project Structure

```
├── annotation       # Custom annotations
├── mapper           # DTO ↔ Entity mappers
├── security         # JWT and security configuration
├── validation       # Validation logic
├── controller       # REST API controllers
├── dto              # Request/response DTOs
├── model            # Database entities (User, Task, Project, etc.)
├── repository       # Spring Data JPA repositories
├── service          # Business logic
├── config           # Configurations (Security, Swagger, Telegram)
├── exception        # Custom exceptions and handlers
└── util             # Utility classes (mappers, validators)
```

---

## 📌 Controllers and Main Endpoints

### 🔐 Auth Controller

* `POST /api/auth/register` — Register a new user
* `POST /api/auth/login` — Authenticate user and return JWT

---

### 👤 Users Controller

* `PUT /api/users/{id}/role` — Change user role (ADMIN only)
* `GET /api/users/me` — Get current user info
* `PUT /api/users/me` — Update user profile
* `PATCH /api/users/me` — Partially update profile

---

### 📁 Project Controller

* `POST /api/projects` — Create a project
* `GET /api/projects` — Get all user's projects
* `GET /api/projects/{id}` — Get project by ID
* `PUT /api/projects/{id}` — Update a project
* `DELETE /api/projects/{id}` — Delete a project

---

### ✅ Task Controller

* `POST /api/tasks` — Create a task
* `GET /api/tasks` — Get tasks (filter by project possible)
* `GET /api/tasks/{id}` — Get task by ID
* `PUT /api/tasks/{id}` — Update a task
* `DELETE /api/tasks/{id}` — Delete a task

---

### 💬 Comment Controller

* `POST /api/comments` — Add a comment to a task
* `GET /api/comments?taskId={taskId}` — Get comments for a task

---

### 📎 Attachment Controller

* `POST /api/attachments` — Upload a file to Dropbox (linked to a task)
* `GET /api/attachments?taskId={taskId}` — List attachments for a task

> Files are stored in Dropbox, only file IDs saved in the database.

---

### 🏷 Label Controller

* `POST /api/labels` — Create a label
* `GET /api/labels` — List all labels
* `PUT /api/labels/{id}` — Update a label
* `DELETE /api/labels/{id}` — Delete a label

---

### 🔔 Telegram Controller

* `GET /api/telegram/info` — Get info about Telegram notification integration

---

## 📬 Postman Collections

For easier API exploration and testing, the repository contains Postman collections.

Import them into your Postman app:

| Entity          | Collection File                                                                 |
| --------------- |---------------------------------------------------------------------------------|
| 🔐 Auth / Users | [User.postman\_collection.json](postman/User.postman_collection.json)           |
| 📁 Projects     | [Project.postman\_collection.json](postman/Project.postman_collection.json)     |
| ✅ Tasks         | [Task.postman\_collection.json](postman/Task.postman_collection.json)           |
| 💬 Comments     | [Comment.postman\_collection.json](postman/Comment.postman_collection.json)     |
| 📎 Attachments  | [Attachment.postman\_collection.json](postman/Attachment.postman_collection.json) |
| 🏷 Labels       | [Label.postman\_collection.json](postman/Label.postman_collection.json)         |

Collections include:

* Registration and login requests
* Project, task, comment, file, and label management
* Full CRUD operations
* JWT-protected requests

---

## 📖 Swagger API Documentation

The project includes interactive API documentation powered by **Swagger (OpenAPI 3)**.

Once the app is running, open in your browser:

🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Swagger UI allows you to:

* View all available endpoints
* See request and response formats
* Execute requests directly from the browser (Try it out!)
* View authorization requirements (JWT)

> 🛡️ To test secured endpoints, click “Authorize” in Swagger and enter your token as:
> `Bearer <your_token_here>`

The documentation is auto-generated using `springdoc-openapi`.

---

## 🧪 Testing

* Unit tests with **JUnit 5** and **Mockito**
* Integration tests using Testcontainers
* `TestUtil` class for test data generation

---

## 📞 Contacts

* **Author:** Mykola Turak
* **GitHub:** [https://github.com/MykolaTurak](https://github.com/MykolaTurak)
* **LinkedIn:** [https://www.linkedin.com/in/mykola-turak-1b6853312/](https://www.linkedin.com/in/mykola-turak-1b6853312/)

---

If you want, I can also help prepare a `.env.sample` file or additional setup instructions!
