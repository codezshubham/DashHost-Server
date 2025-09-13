# DashHost-Server 🚀

[![Java](https://img.shields.io/badge/Java-17-red)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Description 📝

DashHost-Server is a Spring Boot application designed for managing clients, domains, hostings, and credentials. It provides user authentication and authorization using JWT tokens, role-based access control, and activity logging. The application includes features for sending domain expiry notifications via email. It leverages Spring Data JPA for database interactions and provides RESTful APIs for various operations.

## Table of Contents 📚

1.  [Features](#features-%EF%B9%A0)
2.  [Tech Stack](#tech-stack-%E2%9A%92)
3.  [Installation](#installation-%E2%9A%A0)
4.  [Usage](#usage-%F0%9F%9A%80)
5.  [Project Structure](#project-structure-%F0%9F%93%81)
6.  [API Reference](#api-reference-%F0%9F%93%96)
7.  [Contributing](#contributing-%F0%9F%91%A5)
8.  [License](#license-%F0%9F%93%9C)
9.  [Important Links](#important-links-%F0%9F%94%97)
10. [Footer](#footer-%E2%98%80)

## Features ✨

*   **User Authentication and Authorization:** Secure user management with JWT tokens and role-based access control (Admin, User).
*   **Client Management:** Create, retrieve, update, and delete client information.
*   **Domain Management:** Manage domain names, registrars, expiry dates, and statuses.
*   **Hosting Management:** Manage hosting providers, plans, expiry dates, and login URLs.
*   **Credential Management:** Securely store and manage client credentials.
*   **Activity Logging:** Track user activities for auditing and monitoring.
*   **Domain Expiry Notifications:** Automated email notifications for expiring domains.
*   **RESTful APIs:** Comprehensive set of APIs for interacting with the application.

## Tech Stack 💻

*   **Language:** Java
*   **Framework:** Spring Boot
*   **Database:** MySQL
*   **Security:** Spring Security, JWT
*   **Build Tool:** Maven
*   **Other:** Lombok

## Installation 🛠️

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/codezshubham/DashHost-Server.git
    cd DashHost-Server
    ```
2.  **Install Dependencies:**
    *   Make sure you have JDK 17 and Maven installed.
    *   The project uses Maven for dependency management. Dependencies are specified in `pom.xml`.

3.  **Configure the Application:**
    *   Database Configuration: Set up the MySQL database and update the connection details in `src/main/resources/application.properties`.

        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        spring.jpa.hibernate.ddl-auto=update
        ```
    *   Email Configuration: Configure the email settings in `src/main/resources/application.properties` to enable domain expiry notifications.

        ```properties
        spring.mail.host=smtp.gmail.com
        spring.mail.port=587
        spring.mail.username=your_email@gmail.com
        spring.mail.password=your_email_password
        spring.mail.properties.mail.smtp.auth=true
        spring.mail.properties.mail.smtp.starttls.enable=true
        ```
    *   Admin Secret Key: Set up the admin secret key in `src/main/resources/application.properties`.

        ```properties
        admin.secret.key=your_hashed_admin_key
        ```

        Generate a hashed admin key using BCryptPasswordEncoder and set it in application.properties. Example code:
        ```java
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

        public class PasswordGenerator {
            public static void main(String[] args) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String rawPassword = "admin"; // Replace with your admin key
                String encodedPassword = encoder.encode(rawPassword);
                System.out.println("Hashed password: " + encodedPassword);
            }
        }
        ```

4.  **Build the Application:**

    ```bash
    mvn clean install
    ```
5.  **Run the Application:**

    ```bash
    mvn spring-boot:run
    ```

## Usage 🚀

1.  **Signup/Login:**
    *   Use the `/auth/signup` endpoint to register a new user.

        ```json
        {
          "name": "John Doe",
          "email": "john.doe@example.com",
          "password": "password",
          "role": "ROLE_USER"
        }
        ```
    *   Use the `/auth/login` endpoint to log in and obtain a JWT token.

        ```json
        {
          "email": "john.doe@example.com",
          "password": "password",
          "role": "ROLE_USER"
        }
        ```
2.  **Access Protected Endpoints:**
    *   Include the JWT token in the `Authorization` header for accessing protected API endpoints.

        ```
        Authorization: Bearer <JWT_TOKEN>
        ```

3.  **Client Management:**
    *   Create a new client using the `/api/clients/create` endpoint.

        ```json
        {
          "name": "Example Client",
          "contactEmail": "contact@example.com",
          "phone": "123-456-7890",
          "address": "123 Main St"
        }
        ```

    *   Get all clients for a specific user using `/api/clients/user/{userId}`.
    *   Get a client by ID using `/api/clients/{id}`.
    *   Update a client using `/api/clients/update/{id}`.
    *   Delete a client using `/api/clients/delete/{id}`.

4.  **Domain Management:**
    *   Create a new domain using the `/api/domains/create` endpoint.

        ```json
        {
          "client": {"id": 1},
          "domainName": "example.com",
          "registrar": "GoDaddy",
          "expiryDate": "2024-12-31",
          "status": "ACTIVE"
        }
        ```

    *   Get all domains for a specific client using `/api/domains/client/{clientId}`.

5.  **Hosting Management:**
    *   Create a new hosting using the `/api/hostings/create` endpoint.

        ```json
        {
          "client": {"id": 1},
          "provider": "HostGator",
          "expiryDate": "2024-12-31",
          "plan": "Business",
          "loginUrl": "https://hostgator.com/login"
        }
        ```

    *   Get all hostings for a specific client using `/api/hostings/client/{clientId}`.

6.  **Credential Management:**
    *   Create a new credential using the `/api/credentials/create` endpoint.

        ```json
        {
          "client": {"id": 1},
          "type": "cPanel",
          "username": "admin",
          "encryptedPassword": "encrypted"
        }
        ```

    *   Get all credentials for a specific client using `/api/credentials/client/{clientId}`.

## Project Structure 📁

```
DashHost-Server/
├── .mvn/wrapper/maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── WebHost/
│   │   │           └── Manager/
│   │   │               ├── Configuration/
│   │   │               ├── Controller/
│   │   │               ├── DTO/
│   │   │               ├── Exception/
│   │   │               ├── Model/
│   │   │               ├── Repository/
│   │   │               ├── Request/
│   │   │               ├── Response/
│   │   │               ├── Service/
│   │   │               └── domain/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── WebHost/
│                   └── Manager/
│                       └── ManagerApplicationTests.java
```

*   **Configuration:** Contains configuration files for the application, including security configurations, JWT settings, and scheduling.
*   **Controller:** Contains REST controllers for handling API requests related to users, clients, domains, hostings, credentials, and notifications.
*   **DTO:** Contains Data Transfer Objects for transferring data between the client and server.
*   **Exception:** Contains custom exception classes for handling specific error conditions.
*   **Model:** Contains the data models representing entities such as User, Client, Domain, Hosting, and Credential.
*   **Repository:** Contains Spring Data JPA repositories for database interactions.
*   **Request:** Contains request objects for handling incoming data.
*   **Response:** Contains response objects for sending data to the client.
*   **Service:** Contains service classes with business logic for managing entities.
*   **domain:** Contains enum for roles.
*   **application.properties:** Configuration file for Spring Boot application.

## API Reference 🗂️

### Authentication

*   **`POST /auth/signup`:** Registers a new user.
*   **`POST /auth/login`:** Logs in an existing user and returns a JWT token.

### Users

*   **`POST /api/users/create`:** Creates a new user.
*   **`GET /api/users/{id}`:** Retrieves a user by ID.
*   **`GET /api/users/profile`:** Retrieves the current user's profile.
*   **`GET /api/users/email/{email}`:** Retrieves a user by email.
*   **`GET /api/users/all`:** Retrieves all users.
*   **`DELETE /api/users/delete/{id}`:** Deletes a user.
*   **`GET /api/users/my-clients`:** Retrieves clients associated with the current user.

### Clients

*   **`POST /api/clients/create`:** Creates a new client.
*   **`GET /api/clients/{id}`:** Retrieves a client by ID.
*   **`GET /api/clients/all`:** Retrieves all clients.
*   **`GET /api/clients/user/{userId}`:** Retrieves clients for a specific user.
*   **`PUT /api/clients/update/{id}`:** Updates a client.
*   **`DELETE /api/clients/delete/{id}`:** Deletes a client.

### Domains

*   **`POST /api/domains/create`:** Creates a new domain.
*   **`POST /api/domains/add/{clientId}`:** Adds a domain to a specific client.
*   **`GET /api/domains/all`:** Retrieves all domains.
*   **`GET /api/domains/{id}`:** Retrieves a domain by ID.
*   **`GET /api/domains/client/{clientId}`:** Retrieves domains for a specific client.
*   **`GET /api/domains/expired?beforeDate={date}`:** Retrieves expired domains before a specific date.
*   **`PUT /api/domains/update/{id}`:** Updates a domain.
*   **`DELETE /api/domains/delete/{id}`:** Deletes a domain.

### Hostings

*   **`POST /api/hostings/create`:** Creates a new hosting.
*   **`POST /api/hostings/add/{clientId}`:** Adds a hosting to a specific client.
*   **`GET /api/hostings/all`:** Retrieves all hostings.
*   **`GET /api/hostings/{id}`:** Retrieves a hosting by ID.
*   **`GET /api/hostings/client/{clientId}`:** Retrieves hostings for a specific client.
*   **`PUT /api/hostings/update/{id}`:** Updates a hosting.
*   **`DELETE /api/hostings/delete/{id}`:** Deletes a hosting.

### Credentials

*   **`POST /api/credentials/create`:** Creates a new credential.
*   **`POST /api/credentials/add/{clientId}`:** Adds a credential to a specific client.
*   **`GET /api/credentials/all`:** Retrieves all credentials.
*   **`GET /api/credentials/{id}`:** Retrieves a credential by ID.
*   **`GET /api/credentials/client/{clientId}`:** Retrieves credentials for a specific client.
*   **`PUT /api/credentials/update/{id}`:** Updates a credential.
*   **`DELETE /api/credentials/delete/{id}`:** Deletes a credential.

### Notifications

*   **`POST /api/notifications/create`:** Creates a new notification.
*   **`GET /api/notifications/all`:** Retrieves all notifications.
*   **`GET /api/notifications/{id}`:** Retrieves a notification by ID.
*   **`GET /api/notifications/pending`:** Retrieves pending notifications.
*   **`GET /api/notifications/date?date={date}`:** Retrieves notifications by date.
*   **`DELETE /api/notifications/delete/{id}`:** Deletes a notification.

### Activity Logs

*   **`GET /api/logs/all`:** Retrieves all activity logs.
*   **`GET /api/logs/user/{userId}`:** Retrieves activity logs for a specific user.

## Contributing 🤝

Contributions are welcome! Please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes and commit them with descriptive messages.
4.  Submit a pull request.

## License 📜

This project is licensed under the MIT License - see the [LICENSE](https://opensource.org/licenses/MIT) file for details.

## Important Links 🔗

*   **Repository:** [https://github.com/codezshubham/DashHost-Server](https://github.com/codezshubham/DashHost-Server)

## Footer 🗂️


--- 


**DashHost-Server** - [https://github.com/codezshubham/DashHost-Server](https://github.com/codezshubham/DashHost-Server) by codezshubham. Feel free to fork, star, and contribute! Open to issues and suggestions.



<p align="center">[This Readme generated by ReadmeCodeGen.](https://www.readmecodegen.com/)</p>
