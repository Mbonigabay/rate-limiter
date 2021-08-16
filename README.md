Requirements
-----------
1. JDK 11
2. MAVEN
3. MYSQL

How to run the app
------------------
1. Update maven depencies
2. Set MYSQL your database , username and password in application.properties
3. Run the application as a spring boot application

Endpoints
---------
### 1. Sign up <br />
POST http://localhost:8080/api/v1/auth/signup <br />
Content-Type: application/json <br />

```json
{
    "email": "example@xyz.rw",
    "password": "password",
    "confirmPassword": "password"
}
```

### 2. Login <br />
POST http://localhost:8080/api/v1/auth/authenticate <br />
Content-Type: application/json <br />

```json
{
    "email": "example@xyz.rw",
    "password": "password"
}
```
### 3. Change subscription <br />
GET http://localhost:8080/api/v1/auth/subscription/BASIC <br />
Authorization: Bearer **token** <br />

### 4. Send messages (to test the rate limiter) <br />
GET http://localhost:8080/api/v1/messages <br />
Authorization: Bearer **token** <br />