# 🚀 RideShare Backend API

A complete Ride Sharing backend built with Spring Boot, MongoDB, and JWT Authentication.

## 📋 Features

- ✅ User Registration & Login with JWT
- ✅ Role-based Access Control (ROLE_USER / ROLE_DRIVER)
- ✅ Request, Accept, and Complete Rides
- ✅ MongoDB Integration
- ✅ Input Validation
- ✅ Global Exception Handling
- ✅ Clean Architecture (Controller → Service → Repository)

## 🛠️ Tech Stack

- **Spring Boot 3.5.8**
- **Java 21**
- **MongoDB**
- **Spring Security + JWT**
- **Lombok**
- **Maven**

## 📁 Project Structure

```
src/main/java/org/example/rideshare/
├── model/           # Entity classes (User, Ride)
├── repository/      # MongoDB repositories
├── service/         # Business logic
├── controller/      # REST endpoints
├── config/          # Security & JWT configuration
├── dto/             # Data Transfer Objects
├── exception/       # Custom exceptions & handler
└── util/            # JWT utility class
```

## 🚀 Getting Started

### Prerequisites

- Java 21
- MongoDB (running on localhost:27017)
- Maven

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/Ujjwaljain16/rideShare.git
```

2. **Start MongoDB**
Make sure MongoDB is running on `mongodb://localhost:27017`

3. **Run the application**
```bash
.\mvnw spring-boot:run
```

The application will start on `http://localhost:8081`

## 📡 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### User Endpoints (ROLE_USER)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/rides` | Request a new ride |
| GET | `/api/v1/user/rides` | Get user's rides |

### Driver Endpoints (ROLE_DRIVER)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/driver/rides/requests` | View pending ride requests |
| POST | `/api/v1/driver/rides/{id}/accept` | Accept a ride |

### Shared Endpoints (USER/DRIVER)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/rides/{id}/complete` | Complete a ride |

## 📝 API Usage Examples

### 1. Register User
```bash
curl -X POST http://localhost:8081/api/auth/register ^
-H "Content-Type: application/json" ^
-d "{\"username\":\"john\",\"password\":\"1234\",\"role\":\"ROLE_USER\"}"
```

### 2. Register Driver
```bash
curl -X POST http://localhost:8081/api/auth/register ^
-H "Content-Type: application/json" ^
-d "{\"username\":\"driver1\",\"password\":\"abcd\",\"role\":\"ROLE_DRIVER\"}"
```

### 3. Login
```bash
curl -X POST http://localhost:8081/api/auth/login ^
-H "Content-Type: application/json" ^
-d "{\"username\":\"john\",\"password\":\"1234\"}"
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john",
  "role": "ROLE_USER",
  "message": "Login successful"
}
```

### 4. Request a Ride (USER)
```bash
curl -X POST http://localhost:8081/api/v1/rides ^
-H "Authorization: Bearer YOUR_JWT_TOKEN" ^
-H "Content-Type: application/json" ^
-d "{\"pickupLocation\":\"Koramangala\",\"dropLocation\":\"Indiranagar\"}"
```

### 5. View Pending Rides (DRIVER)
```bash
curl -X GET http://localhost:8081/api/v1/driver/rides/requests ^
-H "Authorization: Bearer DRIVER_JWT_TOKEN"
```

### 6. Accept a Ride (DRIVER)
```bash
curl -X POST http://localhost:8081/api/v1/driver/rides/RIDE_ID/accept ^
-H "Authorization: Bearer DRIVER_JWT_TOKEN"
```

### 7. Complete a Ride (USER/DRIVER)
```bash
curl -X POST http://localhost:8081/api/v1/rides/RIDE_ID/complete ^
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 8. Get User's Rides (USER)
```bash
curl -X GET http://localhost:8081/api/v1/user/rides ^
-H "Authorization: Bearer USER_JWT_TOKEN"
```

## 🔐 Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📊 Data Models

### User
```json
{
  "id": "string",
  "username": "string",
  "password": "string (encrypted)",
  "role": "ROLE_USER | ROLE_DRIVER"
}
```

### Ride
```json
{
  "id": "string",
  "userId": "string",
  "driverId": "string | null",
  "pickupLocation": "string",
  "dropLocation": "string",
  "status": "REQUESTED | ACCEPTED | COMPLETED",
  "createdAt": "date"
}
```

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8081

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/rideshare

# JWT
jwt.secret=YOUR_SECRET_KEY
jwt.expiration=86400000
```


## ⚠️ Error Handling

The API returns consistent error responses:

```json
{
  "error": "ERROR_TYPE",
  "message": "Error description",
  "timestamp": "2025-12-07T12:00:00"
}
```

Error Types:
- `VALIDATION_ERROR` - Input validation failed
- `NOT_FOUND` - Resource not found
- `BAD_REQUEST` - Invalid request
- `UNAUTHORIZED` - Authentication failed
- `INTERNAL_ERROR` - Server error

## 📌 Important Notes

- Default MongoDB database: `rideshare`
- JWT tokens expire in 24 hours
- Passwords are encrypted using BCrypt
- All timestamps are in UTC

## 🎯 Assignment Checklist

- ✅ Complete functioning API
- ✅ Proper folder structure
- ✅ DTOs + Validation
- ✅ Exception Handling
- ✅ JWT Auth implemented
- ✅ README with endpoint documentation

