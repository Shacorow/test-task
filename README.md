# Project API Documentation

This documentation provides an overview of the available APIs for **Authentication**, **Task Management**, **User**, and
**Statistic**. It includes how to set up and run application, details on endpoints, request and response formats, as
well as security requirements.

## Table of Contents

[Set Up and Run Application](#set-up-and-run)

1. [Authentication](#authentication)
    - [User Registration](#1-user-registration)
    - [User Login](#2-user-login)
    - [User Logout](#3-user-logout)
2. [Task Management](#task-management)
    - [Create Task](#1-create-task)
    - [Update Task](#2-update-task)
    - [Change Task Status](#3-change-task-status)
    - [List Tasks](#4-list-tasks)
    - [Delete Task](#5-delete-task)
    - [Get Task](#6-get-task)
    - [List Tasks By Status](#7-list-tasks-by-status)
3. [Statistic](#administrator-api)
    - [Get Users Statistics](#1-get-users-statistics)
    - [Export Tasks Statistics](#2-export-tasks-statistics)
    - [Export Users Statistics](#3-export-users-statistics)
    - [List Tasks By Status](#4-list-tasks-by-status)
4. [User](#user)
    - [Find All Users](#1-find-all-users)
    - [Delete User](#2-delete-user)
    - [Change User Role](#3-change-user-role)
    - [Update User](#4-update-user)
5. [Models](#models)
6. [Security](#security)
7. [Error Handling](#error-handling)

---

## Set Up and Run Application

**Requirements** Java-17

**Description:** Before start check **runApplication.bat** and set correct value for db connection. After that you can
run a file **runApplication.bat** to start application. Default admin exist his username and password is **admin**

## Authentication

**Description:** Handles user registration, login, and logout operations

### 1. User Registration

**URL:** `POST /auth/registration`

**Description:** Registers a new user in the system by providing a username and password.

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Responses:**

- **200 OK**: User successfully registered.
    - **Description**: The user has been registered successfully.
    - **Example**:
        ```json
        {
            "username": "string",
            "password": "string"
        }
        ```

- **400 Bad Request**: Invalid input data (e.g., missing username or password).
    - **Description**: The server could not process the request due to client error. This usually happens if the
      required fields like username or password are missing or invalid.
    - **Example**:
        ```json
        {
            "error": "Missing username or password"
        }
        ```

- **409 Conflict**: User already exists.
    - **Description**: A user with the given username already exists in the system, and duplicate registration is not
      allowed.
    - **Example**:
        ```json
        {
            "error": "User already exists"
        }
        ```

### 2. User Login

**URL:** `POST /auth/login`

**Description:** Authenticates a user by validating their credentials and returns a JWT token for session management.

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Responses:**

- **200 OK**: Login successful, returns a JWT token.
    - **Description**: The user has been login successfully.
    - **Example**:
        ```json
        {
            "token": "string"
        }
        ```

- **400 Bad Request**: Invalid input data (e.g., missing username or password).
    - **Description**: The server could not process the request due to client error. This usually happens if the
      required fields like username or password are missing or invalid.
    - **Example**:
        ```json
        {
            "error": "Missing username or password"
        }
        ```

### 3. User Logout

**URL:** `POST /auth/logout`

**Description:** : Logs out the current user by clearing the security context.

**Responses:**

- **200 OK**: Logout successful.
    - **Description**: The user has been logout successfully.

---

## Task Management

**Description**: API for task management

### 1. Create Task

**URL:** `POST /api/task/`

**Description:** Creates a new task in the system.

**Request Body:**

```json
{
  "title": "Task title",
  "description": "Task description",
  "dueDate": "2018-12-31",
  "priority": "LOW"
}
```

**Responses:**

- **200 OK**:  Task successfully created.
    - **Description**: Task has been created successfully.
    - **Example**:
        ```json
        {
          "id": 1,
          "title": "Task title",
          "description": "Task description",
          "dueDate": "2018-12-31",
          "priority": "LOW",
          "status": "NOT_DONE"
        }
        ```

- **400 Bad Request**: Invalid input data.
    - **Description**: The server could not process the request due to client error. This usually happens if the
      required fields like username or password are missing or invalid.
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated

### 2. Update Task

**URL:** `PATCH /api/task/{id}`

**Description:** Updates task details by ID.

**Request Body:**

```json
{
  "title": "Task title",
  "description": "Task description changed",
  "dueDate": "2018-12-31",
  "priority": "LOW"
}
```

**Responses:**

- **200 OK**:  Task successfully updated.
    - **Description**: Task has been updated successfully.
    - **Example**:
        ```json
        {
          "id": 1,
          "title": "Task title",
          "description": "Task description changed",
          "dueDate": "2018-12-31",
          "priority": "LOW",
          "status": "NOT_DONE"
        }
        ```

- **400 Bad Request**: Invalid input data.
    - **Description**: The server could not process the request due to client error. This usually happens if the
      required fields like username or password are missing or invalid.
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **404 Not Found**: Task not found.
    - **Description**: The task with the specified identifier does not exist in the system.
    - **Example**:
        ```json
        {
          "error": "Task not found",
          "message": "No task found with the provided title or ID."
        }
        ```

### 3. Change Task Status

**URL:** `PATCH /api/task/{id}/close`

**Description:** Changes the status of a task by title.

**Responses:**

- **200 OK**:  Task status successfully updated.
    - **Description**: Task status has been updated successfully.
    - **Example**:
        ```json
        {
          "id": 1,
          "title": "Task title",
          "description": "Task description changed",
          "dueDate": "2018-12-31",
          "priority": "LOW",
          "status": "DONE"
        }
        ```

- **400 Bad Request**: Invalid input data.
    - **Description**: The server could not process the request due to client error. This usually happens if the
      required fields like username or password are missing or invalid.
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **404 Not Found**: Task not found.
    - **Description**: The task with the specified identifier (e.g., title or ID) does not exist in the system.
    - **Example**:
        ```json
        {
          "error": "Task not found",
          "message": "No task found with the provided ID."
        }
        ```

### 4. List Tasks

**URL:** `GET /api/task/all`

**Description:** Retrieves a list of user tasks based on their status. If no status is provided, all tasks are returned.

**Responses:**

- **200 OK**:  Get user task successfully.
    - **Description**: User task has been get successfully.
    - **Example**:
        ```json
        [
         {
          "id": 1,
          "title": "Task title",
          "description": "Task description changed",
          "dueDate": "2018-12-31",
          "priority": "LOW",
          "status": "DONE"
         },
         {
          "id": 2,
          "title": "Task title second",
          "description": "Task description second",
          "dueDate": "2018-12-31",
          "priority": "LOW",
          "status": "NOT_DONE"
         }
      ]
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated

### 5. Delete Task

**URL:** `DELETE /api/task/{id}`

**Description:** Deletes user task by its title.

**Responses:**

- **200 OK**:  Task successfully deleted.
    - **Description**: Task has been deleted successfully.
- **404 Not Found**: Task not found.
    - **Description**: The task with the specified identifier does not exist in the system.
    - **Example**:
        ```json
        {
          "error": "Task not found",
          "message": "No task found with the provided ID."
        }
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated

### 6. Get Task

**URL:** `GET /api/task/{id}`

**Description:** Get user task by id.

**Responses:**

- **200 OK**:  Task successfully founded.
    - **Description**: Task has been founded successfully.
    -
        - **Example**:
          ```json
          {
            "id": 1,
            "title": "Task title",
            "description": "Task description changed",
            "dueDate": "2018-12-31",
            "priority": "LOW",
            "status": "DONE"
          }
          ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated          
- **404 Not Found**: Task not found.
    - **Description**: The task with the specified identifier (e.g., title or ID) does not exist in the system.
    - **Example**:
        ```json
        {
          "error": "Task not found",
          "message": "No task found with the provided title or ID."
        }
        ```

### 7. List Tasks By Status

**URL:** `GET /api/task/status/`

**Description:** Returns a list of tasks by status or all tasks without status

**Responses:**

- **200 OK**:  Get user tasks successfully.
    - **Description**: User tasks has been get successfully.
    - **Example**:
        ```json
        [
         {
          "id": 1,
          "title": "Task title",
          "description": "Task description changed",
          "dueDate": "2018-12-31",
          "priority": "LOW",
          "status": "DONE"
         }
      ]
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated

---

## Statistic

**Description**: Admin endpoints for managing users, tasks, and system statistics.

### 1. Get Users Statistics

**URL:** `GET /api/statistic/users`

**Description**: Retrieves statistics about user activities.

**Responses:**

- **200 OK**:  Returns users statistic.
    - **Description**:  Returns users statistic successfully.
    - **Example**:
        ```json
        [ 
         {
          "username": "Dmitriy",
          "countTask": 5
         },
         {
          "username": "Oleg",
          "countTask": 3
         },
         {
          "username": "Admin",
          "countTask": 1
         }
        ]
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **403 Forbidden**: User does not have sufficient permissions.
    - **Description**: User does not have sufficient permissions

### 2. Export Tasks Statistics

**URL:** `GET /api/statistic/export/tasks`

**Description**: Exports task statistics in CSV format.

**Responses:**

- **200 OK**:  Statistics exported successfully.
    - **Description**: Statistics exported successfully.
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **403 Forbidden**: User does not have sufficient permissions.
    - **Description**: User does not have sufficient permissions

### 3. Export Users Statistics

**URL:** `GET /api/statistic/export/users`

**Description**: Exports user statistics in CSV format.

**Responses:**

- **200 OK**:  Statistics exported successfully.
    - **Description**: Statistics exported successfully.
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **403 Forbidden**: User does not have sufficient permissions.
    - **Description**: User does not have sufficient permissions

### 4. List Tasks By Status

**URL:** `GET /api/statistic/tasks/{status}`

**Description:** Retrieves task statistics filtered by the specified status

**Responses:**

- **200 OK**:  Get user tasks successfully.
    - **Description**: User tasks has been get successfully.
    - **Example**:
        ```json
        [
         {
          "id": 1,
          "title": "Task title",
          "description": "Task description changed",
          "dueDate": "2018-12-31",
          "priority": "LOW",
          "status": "DONE"
         }
      ]
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated

---

## User

**Description**: For management user data

### 1. Find All Users

**URL:** `GET /api/user/all`

**Description**: Retrieves a list of all users in the system.

**Responses:**

- **200 OK**: Returns list of users.
    - **Description**: Returns list of users successfully.
    - **Example**:
        ```json
        [
         {
          "id": 1,
          "username": "Dmitriy",
          "password": "qwerty",
          "isAdministrator": false
         },
         {
          "id": 2,
          "username": "Admin",
          "password": "Admin",
          "isAdministrator": true
         }
      ]
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **403 Forbidden**: User does not have sufficient permissions.
    - **Description**: User does not have sufficient permissions

### 2. Delete User

**URL:** `DELETE /api/user/{id}`

**Description**: Deletes a user from the system by their unique ID.

**Responses:**

- **200 OK**:  User successfully deleted.
    - **Description**: User has been deleted successfully.
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **403 Forbidden**: User does not have sufficient permissions.
    - **Description**: User does not have sufficient permissions
- **404 Not Found**: User not found.
    - **Description**: User with the specified identifier does not exist in the system.
    - **Example**:
        ```json
        {
          "error": "User not found",
          "message": "No user found with the provided ID."
        }
        ```
      
### 3. Change User Role

**URL:** `PATCH /api/user/{id}/role`

**Description**: Changes the role of a user identified by their unique ID.

**Responses:**

- **200 OK**:  User successfully deleted.
    - **Description**: User has been deleted successfully.
    - **Example**:
        ```json
        
         {
          "id": 1,
          "username": "Dmitriy",
          "password": "qwerty",
          "isAdministrator": true
         }
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated
- **403 Forbidden**: User does not have sufficient permissions.
    - **Description**: User does not have sufficient permissions
- **404 Not Found**: User not found.
    - **Description**: User with the specified identifier does not exist in the system.
    - **Example**:
        ```json
        {
          "error": "User not found",
          "message": "No user found with the provided username or ID."
        }
        ```
      
### 4. Update User

**URL:** `PATCH /api/user/update`

**Description**: User can update his password

**Responses:**

- **200 OK**: User updates password successfully.
    - **Description**: User updates password successfully.
    - **Example**:
        ```json
   
         {
          "id": 1,
          "username": "Dmitriy",
          "password": "qwerty2",
          "isAdministrator": false
         }
        ```
- **401 Unauthorized**: User unauthorized.
    - **Description**: Unauthorized: User is not authenticated

---

## Models

### TaskDTO

```json
{
  "id": "Long",
  "title": "String",
  "description": "String",
  "dueDate": "Date",
  "priority": "Priority",
  "status": "Status"
}
```

### CreateTaskDTO

```json
{
  "title": "String",
  "description": "String",
  "dueDate": "Date",
  "priority": "Priority"
}
```

### UserDTO

```json
{
  "id": "Long",
  "username": "String",
  "password": "String",
  "isAdministrator": "Boolean",
  "tasks": "MutableList<TaskDTO>"
}
```

### UserStatistic

```json
{
  "username": "String",
  "countTask": "Int"
}
```

### AuthRequest

```json
{
  "username": "String",
  "password": "String"
}
```

---

## Security

All API endpoints that require authentication use **JWT** (JSON Web Tokens) for session management. When logging in, a
valid JWT token is issued and must be included in the **Authorization** header as a `Bearer` token for all subsequent
requests.

### Example Header for Authentication:

```bash
Authorization: Bearer <your_token>
```

---

## Error Handling

The API uses standard HTTP status codes to indicate the success or failure of an API request.

### Common Response Codes:

- **200 OK**: The request was successful.
- **201 Created**: The resource was successfully created.
- **400 Bad Request**: The request is invalid (e.g., missing required parameters).
- **401 Unauthorized**: The user is not authenticated.
- **403 Forbidden**: The user does not have permission to access the resource.
- **404 Not Found**: The resource was not found.
- **409 Conflict**: The request conflicts with an existing resource (e.g., user already exists).
- **500 Internal Server Error**: An unexpected error occurred on the server side.

### Example Error Response:

```json
{
    "error": "Unauthorized",
    "message": "User is not authenticated"
}
