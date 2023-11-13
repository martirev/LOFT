# Loft API

## Base endpoint

Methods:

### GET - check if API is alive

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

### GET - get user info

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

### POST - register user

- URI - host:port/loft/users/{username}/register <http://localhost:8080/loft/users/user123/register>
- parameters
  - password - user password
  - email - user email
  - name - user name
- returns
  - Content-Type: application/json
  - true on success or false on failure

```bash
curl -X POST "localhost:8080/loft/users/user123/register?password=hunter2&email=example%40example.com&name=Test%20Name"
```

```json
true
```

### PUT - update user info

- URI - host:port/loft/users/{username} <http://localhost:8080/loft/users/username>
- parameters
  - password - user password
  - email - user email
  - name - user name
- body
  - Content-Type: application/x-www-form-urlencoded
  - newUsername - new username
  - newName - new name
  - newPassword - new password
  - newEmail - new email
- returns
  - Content-Type: application/json
  - true on success or false on failure

```bash
curl -X PUT "localhost:8080/loft/users/username?password=password&email=email%40domain.com&name=User%20name" -H "Content-Type: application/x-www-form-urlencoded" -d "newUsername=barbaz" -d "newName=Bar%20Baz" -d "newPassword=barbaz" -d "newEmail=bar%40baz.foo"
```

```json
true
```

### PUT - register workout to user

- URI - host:port/loft/users/{username}/workouts <http://localhost:8080/loft/users/user123/workouts>
- parameters
  - password - user password
  - email - user email
  - name - user name
- body
  - Content-Type: application/json
  - json with workout info
- returns
  - Content-Type: application/json
  - true on success or false on failure

```bash
curl -X PUT "localhost:8080/loft/users/user123/workouts?password=hunter2&email=example%40example.com&name=Test%20Name" -H "Content-Type: application/json" -d "{\"exercises\": [],\"date\": \"2023-11-03\"}"
```

```json
true
```

## loft/check-username/

### GET - check if user exists

- URI - host:port/loft/check-username/{username} <http://localhost:8080/loft/check-username/user123>
- parameters - none
- returns
  - Content-Type: application/json
  - true if user exists, false otherwise

```bash
curl localhost:8080/loft/check-username/user123
```

```json
true
```
