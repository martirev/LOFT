# Loft API

## Base endpoint

Methods:

- GET - check if API is alive
  - URI - host:port/loft <http://localhost:8080/loft>
  - parameters - none
  - returns
    - Content-Type: application/json
    - json with boolean true on success

```bash
curl localhost:8080/loft
```

```json
true
```

## loft/users/

<http://localhost:8080/loft/users/{username}>

Methods:

- GET - get user info
  - URI - host:port/loft/users/{username} <http://localhost:8080/loft/users/user123>
  - parameters
    - password - user password
  - returns
    - Content-Type: application/json
    - json with user info

```bash
curl localhost:8080/loft/users/user123?password=hunter2
```

```json
{
  "name": "Test Name",
  "username": "user123",
  "passwordHash": "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
  "email": "example@example.com",
  "workouts": []
}
```

- POST - register user
  - URI - host:port/loft/users/{username}/register <http://localhost:8080/loft/users/user123/register>
  - parameters
    - password - user password
    - email - user email
    - name - user name
  - returns - nothing

```bash
curl -X POST "localhost:8080/loft/users/user123/register" -d "password=passodsad&email=sd&name=ofjhsa+faskj"
```

- PUT - register workout to user
  - URI - host:port/loft/users/{username}/workouts <http://localhost:8080/loft/users/user123/workouts>
  - parameters
    - form-urlencoded
      - password - user password
      - email - user email
      - name - user name
    - body
      - Content-Type: application/json
      - json with workout info
  - returns - nothing

## loft/check-username/

- GET - check if user exists
  - URI - host:port/loft/check-username/{username} <http://localhost:8080/loft/check-username/user123>
  - parameters - none
  - returns
    - Content-Type: application/json
    - json with boolean true if user exists, false otherwise

```bash
curl localhost:8080/loft/check-username/user123
```

```json
true
```
