# Employee Management System

A Spring Boot REST API for employee records, with JWT-based authentication,
role-based access control, and an H2 database.

## Requirements

- Java 21
- Windows PowerShell (commands below use the Maven Wrapper included in this project)

## Configure the JWT signing key

The application needs a Base64-encoded key containing at least 32 bytes. For
local development, it reads the key from `application-secret.properties` in
the project root. This file is ignored by Git.

Create it once in PowerShell:

```powershell
$bytes = New-Object byte[] 32
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
"app.jwt.secret=$([Convert]::ToBase64String($bytes))" | Set-Content application-secret.properties
$rng.Dispose()
```

Do not commit or share this file. In production, provide the key through a
managed secret or environment-specific configuration.

## Run the application

From the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

The API is available at `http://localhost:8080`. Leave the terminal running
while using the API.

Run the tests with:

```powershell
.\mvnw.cmd test
```

## Demo accounts

On first startup, the application creates these accounts if they do not
already exist:

| Role | Username | Default password |
|---|---|---|
| Admin | `admin` | `Admin@12345` |
| Employee | `employee` | `Employee@12345` |

Set `app.demo.admin-password` and `app.demo.employee-password` in
`application-secret.properties` before first startup to change the demo
passwords. Existing initialized accounts are not updated automatically.

The application also initializes `IT` and `Non-IT` departments. Use the
department IDs returned by your database when creating employees.

## API

All request and response bodies use JSON. Login is public; other employee API
requests require a bearer token.

### Login

`POST /api/auth/login`

```json
{
  "username": "admin",
  "password": "Admin@12345"
}
```

The response contains a JWT in the `token` field. Send it on later requests
as `Authorization: Bearer <token>`.

### Employee endpoints

| Method | Path | Access | Success response |
|---|---|---|---|
| `GET` | `/api/employees` | Admin or employee | `200 OK` |
| `POST` | `/api/employees/addEmployee` | Admin | `201 Created` |
| `PUT` | `/api/employees/{id}` | Admin | `200 OK` |
| `DELETE` | `/api/employees/{id}` | Admin | `204 No Content` |

POST and PUT use this request body:

```json
{
  "name": "Alex Employee",
  "email": "alex@example.com",
  "salary": 65000,
  "departmentId": 1
}
```

Names must not be blank, email must be valid, salary must be zero or greater,
and `departmentId` must refer to an existing department. Employee email
addresses must be unique.

## Test with Postman

1. Start the application.
2. Send `POST http://localhost:8080/api/auth/login` with a demo account and
   the JSON body shown above.
3. Copy the response's `token` value.
4. For an employee request, open **Authorization**, select **Bearer Token**,
   and paste the token. Use the endpoint and JSON body from the tables above.
5. To check role-based access, log in as `employee`: listing employees is
   allowed, while creating, updating, or deleting employees is restricted to
   an admin.

An invalid or missing token normally returns `401 Unauthorized`; a valid token
without the required role returns `403 Forbidden`.

## Technology

- Java 21 and Spring Boot
- Spring Web MVC, Spring Data JPA, and Spring Security
- JWT authentication using HS256
- H2 database
- Maven Wrapper
