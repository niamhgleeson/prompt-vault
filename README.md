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
- Spring Security
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

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/prompt-vault.git
```

Move into the project directory:

```bash
cd prompt-vault
```

---

# Creating the Database

Open MySQL:

```bash
mysql -u root -p
```

Create the database:

```sql
CREATE DATABASE promptvault;
```

Exit MySQL:

```sql
EXIT;
```

---

# Importing the Database

The submission includes the file:

```text
database.sql
```

Import it into MySQL:

```bash
mysql -u root -p promptvault < database.sql
```

This will create the application's database tables, including:

- users
- prompts
- prompt_categories
- policy_keywords
- submission_history

and any sample data included with the submission.

---

# Application Configuration

The application uses environment variables for sensitive credentials.

Passwords should not be stored directly in the source code or committed to the Git repository.

The application configuration can be found at:

```text
src/main/resources/application.properties
```

The relevant configuration is:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/promptvault}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

app.admin.username=${PROMPTVAULT_ADMIN_USERNAME:admin}
app.admin.password=${PROMPTVAULT_ADMIN_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

The environment variables must be configured before starting the application.

---

# Configuring the Database Credentials

Set the MySQL username used by the application:

```bash
export DB_USER=root
```

Set the MySQL password:

```bash
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
```

Replace `YOUR_MYSQL_PASSWORD` with the password for the MySQL account being used.

If required, the database URL can also be changed:

```bash
export DB_URL='jdbc:mysql://localhost:3306/promptvault'
```

If `DB_URL` is not specified, the application uses:

```text
jdbc:mysql://localhost:3306/promptvault
```

---

# Configuring the Administrator Account

PromptVault creates a predefined administrator account when the application starts if an administrator with the configured username does not already exist.

The administrator credentials are supplied using environment variables rather than being hardcoded in the application.

Set the administrator username:

```bash
export PROMPTVAULT_ADMIN_USERNAME=admin
```

Set the administrator password:

```bash
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
```

Replace `YOUR_ADMIN_PASSWORD` with the password that should be used for the administrator account.

For example, before starting the application, the required environment variables can be configured with:

```bash
export DB_USER=root
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
export PROMPTVAULT_ADMIN_USERNAME=admin
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
```

These environment variables apply to the current terminal session.

If a new terminal is opened, they must be set again before running the application.

---

# Important Note About an Existing Administrator

The administrator is created only if an account with the configured administrator username does not already exist.

For example, if the database already contains:

```text
admin
```

changing:

```text
PROMPTVAULT_ADMIN_PASSWORD
```

will not automatically change the password of that existing account.

This prevents the administrator password from being reset every time the application starts.

When starting with a fresh database that does not already contain the administrator, the administrator account will be created using the password supplied in:

```text
PROMPTVAULT_ADMIN_PASSWORD
```

The password is encoded using BCrypt before it is stored in the database.

---

# Running the Application

Before running the application, ensure that:

1. MySQL is running.
2. The `promptvault` database has been created.
3. `database.sql` has been imported if required.
4. The database environment variables have been configured.
5. The administrator environment variables have been configured.

For example:

```bash
export DB_USER=root
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
export PROMPTVAULT_ADMIN_USERNAME=admin
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
```

Then run the application from the project directory:

```bash
./mvnw spring-boot:run
```

Alternatively, the project can be opened in IntelliJ IDEA and run using the `PromptVaultApplication` class.

When the application has started successfully, open:

```text
http://localhost:8080
```

The application will display the PromptVault login page.

---

# Administrator Login

The default administrator username is:

```text
admin
```

The administrator password is **not stored in this repository**.

It is the password supplied before first startup using:

```text
PROMPTVAULT_ADMIN_PASSWORD
```

For example:

```bash
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
```

The application stores the administrator password as a BCrypt hash rather than plaintext.

---

# Creating a User Account

A normal user account can be created through the application's Register page:

```text
http://localhost:8080/register-page
```

Registered users are assigned the `USER` role.

After registration, the user can log in through:

```text
http://localhost:8080/login-page
```

---

# Spring Security

The application uses Spring Security for authentication, authorization and session management.

Public pages include:

- Login
- Registration

Other application pages require authentication.

Administrator functionality is restricted to users with the `ADMIN` role.

Normal users cannot access administrator functionality.

Users are also restricted to operations on prompts that they own. For example, a user cannot edit or delete another user's private prompt simply by changing the prompt ID in the URL.

Passwords are stored as BCrypt hashes.

The application also uses CSRF protection for state-changing requests such as form submissions and logout.

---

# Application Pages

## Public Pages

Login:

```text
/login-page
```

Register:

```text
/register-page
```

## User Pages

User Dashboard:

```text
/user-dashboard
```

User Prompts:

```text
/user-prompts-page
```

Shared Prompts:

```text
/shared-prompts-page
```

Submission History:

```text
/user-history-page
```

## Administrator Pages

Admin Dashboard:

```text
/admin-dashboard
```

Admin Users:

```text
/admin-users-page
```

Prompt Categories:

```text
/admin-categories-page
```

Policy Keywords:

```text
/admin-keywords-page
```

Flagged Prompts:

```text
/admin-flagged-prompts-page
```

---

# Simulated AI

The application does not contact an external AI service.

When a user submits one of their prompts, the application generates a simulated AI response.

If the prompt contains a configured policy keyword:

- the user receives a warning
- the prompt is flagged
- the matched policy keyword is recorded
- the flagged prompt becomes visible to the administrator

Submission information is recorded in the user's submission history.

---

# Security Notes

PromptVault uses a number of security controls, including:

- Spring Security authentication
- Role-based authorization
- BCrypt password hashing
- Environment variables for sensitive credentials
- CSRF protection
- Session-based authentication
- Ownership checks for user prompts
- Restricted administrator endpoints
- Disabled-account enforcement

Sensitive credentials such as the administrator password and database password should never be committed to the repository.

---

# Quick Start

For a fresh installation, the basic process is:

```bash
git clone https://github.com/YOUR_USERNAME/prompt-vault.git

cd prompt-vault

mysql -u root -p
```

Inside MySQL:

```sql
CREATE DATABASE promptvault;
EXIT;
```

Then import the supplied database script:

```bash
mysql -u root -p promptvault < database.sql
```

Set the required environment variables:

```bash
export DB_USER=root
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
export PROMPTVAULT_ADMIN_USERNAME=admin
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
```

Run PromptVault:

```bash
./mvnw spring-boot:run
```

Open:

```text
http://localhost:8080
```

The application is now ready to use.