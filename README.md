# PromptVault

## COMP47910 – Secure Software Engineering 2026

Author: Niamh Gleeson

---

# Project Description

PromptVault is a Spring Boot web application that allows users to create, manage and submit prompts to a simulated AI assistant.

The application supports two roles:

- Administrator
- User

The administrator can:

- Login and logout
- View all registered users
- Enable and disable user accounts
- Add, edit and delete prompt categories
- Add, edit and delete policy keywords
- View prompts that have been flagged for containing sensitive information

Users can:

- Register
- Login and logout
- Create prompts
- Edit their own prompts
- Delete their own prompts
- View their own prompts
- Browse shared prompts
- Submit prompts to a simulated AI assistant
- Receive warnings if prompts contain sensitive keywords
- View their own prompt submission history

The application uses a simulated AI response and does not connect to an external AI API.

---

# Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Thymeleaf
- Maven

---

# Prerequisites

The following software must be installed before running the application.

## Java

Install Java 21.

Check the installation:

```bash
java --version
```

---

## Git

Install Git.

Ubuntu:

```bash
sudo apt install git
```

Check the installation:

```bash
git --version
```

---

## MySQL

Install MySQL Server.

Ubuntu:

```bash
sudo apt update
sudo apt install mysql-server
```

Start MySQL:

```bash
sudo systemctl start mysql
```

Check that it is running:

```bash
sudo systemctl status mysql
```

---

# Downloading the Project

Clone the repository.

```bash
git clone https://github.com/YOUR_USERNAME/prompt-vault.git
```

Move into the project directory.

```bash
cd prompt-vault
```

---

# Creating the Database

Open MySQL.

```bash
mysql -u root -p
```

Create the database.

```sql
CREATE DATABASE promptvault;
```

Exit MySQL.

```sql
EXIT;
```

---

# Importing the Database

The submission includes the file

```
database.sql
```

Import it into MySQL.

```bash
mysql -u root -p promptvault < database.sql
```

This will create:

- users
- prompts
- prompt_categories
- policy_keywords
- submission_history

and insert the sample data.

---

# Configuring the Application

Open

```
src/main/resources/application.properties
```

Update the database connection if necessary.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/promptvault
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

Replace

```
YOUR_PASSWORD
```

with your MySQL password.

---

# Running the Application

From the project directory run

```bash
./mvnw spring-boot:run
```

Alternatively, the project can be opened in IntelliJ IDEA and run using the PromptVaultApplication class.

When the application has started successfully, open

```
http://localhost:8080
```

---

# Default Administrator

The application includes a predefined administrator.

Username

```
admin
```

Password

```
admin123
```

---

# Suggested Test Users

The imported database includes multiple user accounts.

Alternatively, create a new account using the Register page.

Test User Username

```
alice1
```

Password

```
abc123
```


---

# Application Pages

Login

```
/login-page
```

Register

```
/register-page
```

User Dashboard

```
/user-dashboard
```

Admin Dashboard

```
/admin-dashboard
```

User Prompts

```
/user-prompts-page
```

Shared Prompts

```
/shared-prompts-page
```

Submission History

```
/user-history-page
```

Admin Users

```
/admin-users-page
```

Prompt Categories

```
/admin-categories-page
```

Policy Keywords

```
/admin-keywords-page
```

Flagged Prompts

```
/admin-flagged-prompts-page
```

---

# Simulated AI

The application does not contact an external AI service.

Submitting a prompt returns a simulated response.

If the prompt contains a policy keyword:

- the user receives a warning
- the prompt is flagged
- the flagged prompt becomes visible to the administrator

---
