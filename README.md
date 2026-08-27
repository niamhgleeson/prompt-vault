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

- Log in and log out
- View registered users
- Enable and disable user accounts
- Add, edit and delete prompt categories
- Add, edit and delete policy keywords
- View prompts that have been flagged for containing sensitive information

Users can:

- Register
- Log in and log out
- Create prompts
- Edit their own prompts
- Delete their own prompts
- View their own prompts
- Browse shared prompts
- Submit prompts to a simulated AI assistant
- Receive warnings when prompts contain configured sensitive keywords
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
- Snyk
- GitHub Actions

---

# Prerequisites

The following software is required to run the application:

- Java 21
- Git
- MySQL Server

## Java

Check the installed Java version:

```bash
java --version
```

## Git

On Ubuntu:

```bash
sudo apt install git
```

Check the installation:

```bash
git --version
```

## MySQL

Install MySQL Server:

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

The submission includes:

```text
database.sql
```

Import it with:

```bash
mysql -u root -p promptvault < database.sql
```

The script creates the application's database structure and any supplied sample data.

The main application tables include:

- users
- prompts
- prompt_categories
- policy_keywords
- submission_history

---

# Application Configuration

PromptVault uses environment variables for sensitive configuration values.

Passwords and secrets should not be stored directly in source code or committed to the Git repository.

The main Spring Boot configuration is located at:

```text
src/main/resources/application.properties
```

Database configuration uses environment variables such as:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/promptvault}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

app.admin.username=${PROMPTVAULT_ADMIN_USERNAME:admin}
app.admin.password=${PROMPTVAULT_ADMIN_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

The application is also configured to use HTTPS and secure session cookies.

---

# Configuring Database Credentials

Set the MySQL username:

```bash
export DB_USER=root
```

Set the MySQL password:

```bash
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
```

If required, set a different database URL:

```bash
export DB_URL='jdbc:mysql://localhost:3306/promptvault'
```

If `DB_URL` is not specified, the default database URL is:

```text
jdbc:mysql://localhost:3306/promptvault
```

---

# Configuring the Administrator Account

PromptVault creates a predefined administrator account when the application starts if an administrator with the configured username does not already exist.

Administrator credentials are supplied using environment variables rather than being hardcoded in source code.

Set the administrator username:

```bash
export PROMPTVAULT_ADMIN_USERNAME=admin
```

Set the administrator password:

```bash
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
```

The administrator password is encoded using BCrypt before being stored in the database.

## Existing Administrator Accounts

The administrator is only created if an account with the configured administrator username does not already exist.

Therefore, changing:

```text
PROMPTVAULT_ADMIN_PASSWORD
```

does not automatically change the password of an administrator account that already exists in the database.

When starting with a fresh database, the administrator is created using the password supplied through the environment variable.

---

# HTTPS Configuration

PromptVault is configured to run locally using HTTPS.

The local TLS certificate is stored in a Java keystore. The keystore password must be supplied using an environment variable rather than being committed to the repository.

Set the keystore password:

```bash
export KEYSTORE_PASSWORD='YOUR_KEYSTORE_PASSWORD'
```

The application runs at:

```text
https://localhost:8080
```

Because the project uses a locally generated development certificate, the browser may display a certificate or "Not secure" warning.

This is expected for the local development environment because the certificate is not signed by a publicly trusted Certificate Authority.

Do not disable HTTPS in order to remove this warning.

For a production deployment, a certificate issued by a trusted Certificate Authority should be used.

---

# Setting All Required Environment Variables

Before running PromptVault, configure the required environment variables.

For example:

```bash
export DB_USER=root
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
export PROMPTVAULT_ADMIN_USERNAME=admin
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
export KEYSTORE_PASSWORD='YOUR_KEYSTORE_PASSWORD'
```

These environment variables apply to the current terminal session.

If a new terminal is opened, they must be configured again.

A local helper script may be used to set development environment variables, but any script containing real credentials must be excluded from version control using `.gitignore`.

Never commit real database, administrator or keystore passwords to the repository.

---

# Running the Application

Before running the application, ensure that:

1. MySQL is running.
2. The `promptvault` database exists.
3. `database.sql` has been imported if required.
4. Database environment variables have been configured.
5. Administrator environment variables have been configured.
6. The keystore password environment variable has been configured.

Run the application from the project directory:

```bash
./mvnw spring-boot:run
```

Alternatively, open the project in IntelliJ IDEA and run:

```text
PromptVaultApplication
```

When the application starts successfully, open:

```text
https://localhost:8080
```

The PromptVault login page will be displayed.

---

# Administrator Login

The configured administrator username defaults to:

```text
admin
```

The administrator password is not stored in this repository.

It is supplied before first startup using:

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

A normal user account can be created through:

```text
https://localhost:8080/register-page
```

Registered users are assigned the `USER` role.

After registration, users can log in at:

```text
https://localhost:8080/login-page
```

Registration input is validated before an account is created.

---

# Application Pages

## Public Pages

Login:

```text
/login-page
```

Registration:

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

Administrator pages require an authenticated account with the `ADMIN` role.

---

# Simulated AI

PromptVault does not contact an external AI service.

When a user submits one of their prompts, the application generates a simulated AI response.

If the prompt contains a configured policy keyword:

- the user receives a warning
- the prompt is flagged
- the matched policy keyword is recorded
- the flagged submission can be reviewed by an administrator

Submission information is recorded in the user's submission history.

---

# Security Controls

PromptVault uses Spring Security for authentication, authorization and session management.

Implemented security controls include:

- Spring Security authentication
- Role-based authorization
- Administrator-only route protection
- Server-side authenticated-principal identity
- Prompt ownership checks
- BCrypt password hashing
- HTTPS/TLS
- Secure session cookies
- HttpOnly session cookies
- SameSite session cookie protection
- Cookie-only session tracking
- Session fixation protection
- Session invalidation on logout
- CSRF protection
- Content Security Policy
- Anti-clickjacking protection
- MIME-sniffing protection
- Input validation
- Purpose-built request DTOs
- Environment variables for sensitive configuration
- Login rate limiting
- Prompt submission rate limiting
- Security audit logging
- Request correlation IDs
- Generic exception handling
- Automated dependency scanning with Snyk

Users cannot gain administrator privileges by supplying an administrator ID in a request.

Users are also restricted to operations on prompts they own. Server-side ownership checks are used rather than trusting client-supplied ownership information.

---

# Session Security

PromptVault uses session-based authentication.

Session security includes:

- HTTPS transport
- `Secure` session cookies
- `HttpOnly` session cookies
- `SameSite=Lax`
- cookie-only session tracking
- session fixation protection
- session invalidation during logout

Session identifiers should never be placed in URLs, logs or documentation.

---

# Rate Limiting

Rate limiting is implemented as an additional defence against automated abuse.

Login rate limiting reduces repeated authentication attempts.

Prompt submission rate limiting reduces excessive automated prompt submissions.

When a configured limit is exceeded, the application rejects further requests until the rate-limit period has elapsed.

---

# Security Logging

Security-relevant events are recorded through the application's security audit logging functionality.

Examples include:

- failed login attempts
- rate-limited requests
- flagged prompt submissions
- unauthorized prompt-access attempts

Sensitive values such as passwords, session identifiers, CSRF tokens and complete sensitive prompt contents are not intentionally written to security audit logs.

Each HTTP request is also assigned a request correlation identifier.

The identifier is returned using:

```text
X-Request-ID
```

This assists with correlating related application and security events.

---

# Dependency Security

Project dependencies can be checked using Snyk.

If Snyk CLI is installed and authenticated, run:

```bash
snyk test
```

The repository also contains a GitHub Actions workflow that performs automated dependency security scanning.

The Snyk authentication token used by GitHub Actions must be stored as a GitHub repository secret and must not be committed to the repository.

---

# Local Development Secrets

Real credentials must not be committed to Git.

Files containing local credentials, such as a local startup script, should be listed in:

```text
.gitignore
```

For example:

```gitignore
run-local.sh
```

A local `run-local.sh` may be used to export development environment variables before starting the application, but the file must remain local if it contains real credentials.

Example structure:

```bash
#!/bin/bash

export DB_USER='YOUR_DB_USER'
export DB_PASSWORD='YOUR_DB_PASSWORD'
export PROMPTVAULT_ADMIN_USERNAME='admin'
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
export KEYSTORE_PASSWORD='YOUR_KEYSTORE_PASSWORD'

./mvnw spring-boot:run
```

Do not commit a version of this file containing real passwords.

---

# Quick Start

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/prompt-vault.git

cd prompt-vault
```

Create the database:

```bash
mysql -u root -p
```

Then:

```sql
CREATE DATABASE promptvault;
EXIT;
```

Import the supplied database:

```bash
mysql -u root -p promptvault < database.sql
```

Configure the required environment variables:

```bash
export DB_USER=root
export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
export PROMPTVAULT_ADMIN_USERNAME=admin
export PROMPTVAULT_ADMIN_PASSWORD='YOUR_ADMIN_PASSWORD'
export KEYSTORE_PASSWORD='YOUR_KEYSTORE_PASSWORD'
```

Run PromptVault:

```bash
./mvnw spring-boot:run
```

Open:

```text
https://localhost:8080
```

If the browser displays a certificate warning, this is expected when using the local development certificate.

PromptVault is now ready to use.

---

# Security Testing

The application was security tested using:

- Snyk Open Source
- Snyk Code
- OWASP ZAP
- Manual authorization and access-control testing

The remediation report supplied with the project documents the identified vulnerabilities, implemented security controls, OWASP Top 10 mappings and final verification results.

---

# Important Security Notice

This project is a coursework application intended for local development and security testing.

Development credentials, local keystores and other secrets should not be treated as production credentials.

For a production deployment, additional controls should be considered, including:

- a publicly trusted TLS certificate
- production-grade secret management
- persistent/distributed rate limiting where required
- centralized security log collection and alerting
- multi-factor authentication for privileged accounts
- production database and infrastructure hardening