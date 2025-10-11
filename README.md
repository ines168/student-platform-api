
# Auth & JWT Module (Java + Spring Boot)

This is the authentication and authorization module, built with **Java Spring Boot**. It includes:

* ✅ JWT-based login
* ✅ Refresh token implementation
* ✅ Secure password hashing with BCrypt
* ✅ Email verification
* ✅ Password reset via email link
* ✅ User authentication filter
* ✅ Logout & token invalidation

---

##  Features Implemented

### ✅ User Registration

* Email and password
* Bcrypt password hashing
* Role assignment (STUDENT by default)
* Sends email verification link

### ✅ JWT Authentication

* Login with email + password
* Issue short-lived **access token**
* Uses `io.jsonwebtoken` for JWT creation/validation

### ✅ Refresh Token System

* Issue long-lived **refresh token**
* Save refresh token in DB (`refresh_tokens` table)
* Endpoint to refresh access token
* Expiration handling and invalidation

### ✅ Email Verification

* Sends verification link after registration
* Verifies email via token in URL
* Token is time-limited
* Resend verification link endpoint

### ✅ Logout

* Deletes the user's refresh token
* Requires authentication (via JWT)

### ✅ Authentication Filter

* Custom JWT filter extracts and validates token
* Injects authenticated `User` into controller via `@AuthenticationPrincipal`

### ✅ Password Reset

* Request password reset via email
* Email contains time-limited token link
* Secure password update via /reset-password
* Tokens stored in `password_reset_token` table

---

##  API Endpoints

###  Auth Endpoints

| Method | Endpoint                        | Description                         |
|--------|---------------------------------|-------------------------------------|
| POST   | `/api/auth/register`            | Register new user                   |
| POST   | `/api/auth/login`               | Authenticate and get tokens         |
| POST   | `/api/auth/refresh`             | Get new access token                |
| POST   | `/api/auth/logout`              | Logout & revoke refresh token       |


###  Email

| Method | Endpoint                        | Description                         |
|--------|---------------------------------|-------------------------------------|
| GET    | `/api/auth/verify-email`        | Verify email using token (via link) |
| POST   | `/api/auth/resend-verification` | Resend verification email           |

###  Password

| Method | Endpoint                   | Description                      |
|--------|----------------------------|----------------------------------|
| POST   | `/api/auth/request-reset`  | Request password reset via email |
| GET    | `/api/auth/reset-password` | Serve reset form                 |
| POST   | `/api/auth/reset-password` | Reset password using token       |

---

##  Security Setup

* JWT validated on all protected routes
* Unauthenticated access only allowed for:

    * `/api/auth/**`
* Custom `JwtAuthFilter` parses and validates token from `Authorization: Bearer <token>` header
* Injects user into controller via `@AuthenticationPrincipal`

---

##  Database Setup

### Tables Used:

* `users`
* `refresh_token`
* `verification_token`
* `password_reset_token`

---

##  Next Up (Planned Features)

* Profile management endpoints
* Rate limiting on login/register
* Role-based field validation

---

##  Tech Stack

* Java
* Spring Boot
* Spring Security
* JPA + MySQL
* JWT (`io.jsonwebtoken`)
* BCrypt (Spring Security)
* JavaMail

---