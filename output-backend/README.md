# Library Management System — Spring Boot Backend (React-Ready)

This is the **updated** Spring Boot backend wired to the React frontend (`library-ms`).

---

## What Was Updated

All previously empty stub files have been fully implemented:

| Layer | Files Updated |
|---|---|
| **Models** | `SignupModel`, `BookModel`, `IssueModel`, `ContactModel` — JPA entities |
| **Repositories** | `signupRepository`, `BookRepository`, `IssueRepository`, `ContactRepository` — Spring Data JPA |
| **Services** | `Signupservice`, `Bookservice`, `IssueService`, `ContactService` — business logic |
| **Controllers** | `SignupController`, `BookController`, `IssuseController`, `ContactController`, `LibraryController` |
| **Security (NEW)** | `JwtUtil`, `JwtAuthFilter` — JWT token generation & validation |
| **Config (NEW)** | `SecurityConfig` — Spring Security + role-based access, `CorsConfig` — CORS for React |
| **DTOs (NEW)** | `ApiResponse`, `RegisterRequest`, `LoginRequest`, `LoginResponse`, `UserDTO`, `BookRequest`, `BorrowRequestDTO` |
| **pom.xml** | Added: Spring Security, JJWT, Validation, H2 (dev), downgraded to Boot 3.2.5 |
| **application.properties** | Database, JWT, server port (5000), borrow/reservation config |

---

## API Endpoints (matching React `libraryApi.js`)

### Auth — `/api/auth`
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| GET  | `/api/auth/me` | JWT | Get current user profile |

### Books — `/api/books`
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET    | `/api/books` | Public | List all books (optional `?title=&author=&genre=`) |
| GET    | `/api/books/{id}` | Public | Get book by ID |
| POST   | `/api/books` | Admin | Add new book |
| PUT    | `/api/books/{id}` | Admin | Update book |
| DELETE | `/api/books/{id}` | Admin | Delete book |

### Borrow Requests — `/api/requests`
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST   | `/api/requests` | User | Submit borrow request |
| GET    | `/api/requests/my` | User | View own requests |
| GET    | `/api/requests` | Admin | View all requests (optional `?status=pending`) |
| PATCH  | `/api/requests/{id}/approve` | Admin | Approve request |
| PATCH  | `/api/requests/{id}/reject` | Admin | Reject request |
| PATCH  | `/api/requests/{id}/collected` | Admin | Mark as collected |
| PATCH  | `/api/requests/{id}/returned` | Admin | Mark as returned |

### Contact — `/api/contact`
| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/contact` | Public | Submit support ticket |
| GET  | `/api/contact` | Admin | View all tickets |

### Health
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/health` | Returns `{"success":true,"data":{"status":"ok"}}` |

---

## Quick Start

### Option A — H2 (No MySQL needed, for dev)
```bash
./mvnw spring-boot:run
```
- App runs on `http://localhost:5000`
- H2 console available at `http://localhost:5000/h2-console`
  - JDBC URL: `jdbc:h2:file:./data/library_db`

### Option B — MySQL
Edit `application.properties`:
1. Comment out the H2 block
2. Uncomment and fill the MySQL block with your credentials
3. Create database: `CREATE DATABASE library_db;`

### Running with the React frontend
```bash
# Terminal 1 — Backend
cd Library-Management(React)
./mvnw spring-boot:run

# Terminal 2 — Frontend
cd Library_Management/library-ms
npm install
npm run dev
```
React uses `VITE_API_URL=http://localhost:5000` (default in `libraryApi.js`).

---

## Response Format
All endpoints return:
```json
{ "success": true,  "data": { ... } }
{ "success": false, "error": "Message" }
```
This matches the React frontend's `apiFetch` handler exactly.
