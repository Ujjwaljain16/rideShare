# 🧪 RideShare API - Testing Guide

## Prerequisites

1. **Start MongoDB**
   ```powershell
   # Make sure MongoDB is running on localhost:27017
   mongod
   ```

2. **Navigate to project directory**
   ```powershell
   cd c:\Users\ujjwa\OneDrive\Desktop\SpringBoot\rideshare
   ```

3. **Run the application**
   ```powershell
   .\mvnw spring-boot:run
   ```

   The application will start on: `http://localhost:8081`

---

## 📝 Testing Flow (Step-by-Step)

### Step 1: Register a User (Passenger)

**Request:**
```powershell
curl -X POST http://localhost:8081/api/auth/register `
-H "Content-Type: application/json" `
-d '{\"username\":\"john\",\"password\":\"1234\",\"role\":\"ROLE_USER\"}'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john",
  "role": "ROLE_USER",
  "message": "User registered successfully"
}
```

**Action:** Save the `token` value for Step 3

---

### Step 2: Register a Driver

**Request:**
```powershell
curl -X POST http://localhost:8081/api/auth/register `
-H "Content-Type: application/json" `
-d '{\"username\":\"driver1\",\"password\":\"abcd\",\"role\":\"ROLE_DRIVER\"}'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "driver1",
  "role": "ROLE_DRIVER",
  "message": "User registered successfully"
}
```

**Action:** Save the `token` value for Step 4

---

### Step 3: User Requests a Ride

**Request:** (Replace `YOUR_USER_TOKEN` with the token from Step 1)
```powershell
curl -X POST http://localhost:8081/api/v1/rides `
-H "Authorization: Bearer YOUR_USER_TOKEN" `
-H "Content-Type: application/json" `
-d '{\"pickupLocation\":\"Koramangala\",\"dropLocation\":\"Indiranagar\"}'
```

**Expected Response:**
```json
{
  "id": "675471234567890abcdef123",
  "userId": "675470123456789abcdef000",
  "driverId": null,
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar",
  "status": "REQUESTED",
  "createdAt": "2025-12-07T10:30:00.000+00:00"
}
```

**Action:** Save the `id` value (this is the rideId) for Step 5

---

### Step 4: Driver Views Pending Rides

**Request:** (Replace `YOUR_DRIVER_TOKEN` with the token from Step 2)
```powershell
curl -X GET http://localhost:8081/api/v1/driver/rides/requests `
-H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

**Expected Response:**
```json
[
  {
    "id": "675471234567890abcdef123",
    "userId": "675470123456789abcdef000",
    "driverId": null,
    "pickupLocation": "Koramangala",
    "dropLocation": "Indiranagar",
    "status": "REQUESTED",
    "createdAt": "2025-12-07T10:30:00.000+00:00"
  }
]
```

---

### Step 5: Driver Accepts the Ride

**Request:** (Replace `YOUR_DRIVER_TOKEN` and `RIDE_ID` with your values)
```powershell
curl -X POST http://localhost:8081/api/v1/driver/rides/RIDE_ID/accept `
-H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

**Expected Response:**
```json
{
  "id": "675471234567890abcdef123",
  "userId": "675470123456789abcdef000",
  "driverId": "675470987654321fedcba000",
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar",
  "status": "ACCEPTED",
  "createdAt": "2025-12-07T10:30:00.000+00:00"
}
```

---

### Step 6: Complete the Ride

**Request:** (Can be done by either User or Driver)
```powershell
# Using Driver Token
curl -X POST http://localhost:8081/api/v1/rides/RIDE_ID/complete `
-H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

**Expected Response:**
```json
{
  "id": "675471234567890abcdef123",
  "userId": "675470123456789abcdef000",
  "driverId": "675470987654321fedcba000",
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar",
  "status": "COMPLETED",
  "createdAt": "2025-12-07T10:30:00.000+00:00"
}
```

---

### Step 7: User Views Their Rides

**Request:** (Replace `YOUR_USER_TOKEN` with user token)
```powershell
curl -X GET http://localhost:8081/api/v1/user/rides `
-H "Authorization: Bearer YOUR_USER_TOKEN"
```

**Expected Response:**
```json
[
  {
    "id": "675471234567890abcdef123",
    "userId": "675470123456789abcdef000",
    "driverId": "675470987654321fedcba000",
    "pickupLocation": "Koramangala",
    "dropLocation": "Indiranagar",
    "status": "COMPLETED",
    "createdAt": "2025-12-07T10:30:00.000+00:00"
  }
]
```

---

## 🔍 Testing Validation Errors

### Test 1: Register with Short Username
```powershell
curl -X POST http://localhost:8081/api/auth/register `
-H "Content-Type: application/json" `
-d '{\"username\":\"ab\",\"password\":\"1234\",\"role\":\"ROLE_USER\"}'
```

**Expected:**
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Input validation failed",
  "fields": {
    "username": "Username must be between 3 and 20 characters"
  },
  "timestamp": "2025-12-07T10:30:00"
}
```

### Test 2: Request Ride Without Pickup Location
```powershell
curl -X POST http://localhost:8081/api/v1/rides `
-H "Authorization: Bearer YOUR_USER_TOKEN" `
-H "Content-Type: application/json" `
-d '{\"dropLocation\":\"Indiranagar\"}'
```

**Expected:**
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Input validation failed",
  "fields": {
    "pickupLocation": "Pickup location is required"
  },
  "timestamp": "2025-12-07T10:30:00"
}
```

### Test 3: Access Protected Endpoint Without Token
```powershell
curl -X GET http://localhost:8081/api/v1/user/rides
```

**Expected:** HTTP 401 Unauthorized

---

## 🚨 Common Issues & Solutions

### Issue 1: MongoDB Connection Error
```
Error: MongoTimeoutException
```
**Solution:** Ensure MongoDB is running on port 27017

### Issue 2: Port Already in Use
```
Error: Port 8081 is already in use
```
**Solution:** Change port in `application.properties` or kill the process using port 8081

### Issue 3: JWT Token Invalid
```
{
  "error": "UNAUTHORIZED",
  "message": "JWT Token extraction failed"
}
```
**Solution:** Make sure the token is correctly copied and prefixed with "Bearer "

---

## 📊 Testing Checklist

- [ ] User registration works
- [ ] Driver registration works
- [ ] Login returns JWT token
- [ ] User can request a ride
- [ ] Driver can view pending rides
- [ ] Driver can accept a ride
- [ ] User/Driver can complete a ride
- [ ] User can view their rides
- [ ] Validation errors are properly returned
- [ ] Unauthorized access is blocked
- [ ] Role-based access control works

---

## 🎯 Postman Testing

1. Import the `RideShare-API.postman_collection.json` file into Postman
2. Collection variables are automatically set:
   - `baseUrl`: http://localhost:8081
   - `userToken`: Auto-saved after user login
   - `driverToken`: Auto-saved after driver login
   - `rideId`: Auto-saved after creating a ride

3. Run the requests in order:
   - Authentication → Register User
   - Authentication → Register Driver
   - Authentication → Login User (token auto-saved)
   - Authentication → Login Driver (token auto-saved)
   - User Operations → Request Ride (rideId auto-saved)
   - Driver Operations → View Pending Rides
   - Driver Operations → Accept Ride
   - Shared Operations → Complete Ride

---

**Happy Testing! 🚀**
