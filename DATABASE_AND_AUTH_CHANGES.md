# Database and Authentication Changes

This document outlines the recent changes made to the project, focusing on the integration of a new database and enhancements to the user authentication system.

## Summary of Changes

### 1. Database Integration
- **MySQL Dependency:** The `mysql-connector-j` dependency was added to the `pom.xml` file. This allows the application to connect to a MySQL database.
- **Database Configuration:** The `application.properties` file has been updated with the necessary connection details for the new database. The previous SQL Server configuration has been commented out.

### 2. Authentication Enhancements
- **User Role in Token:** The `AuthController` has been modified to include the user's role in the ID token returned upon successful login. This allows the frontend to have immediate access to the user's role without making a separate API call.
- **Retrieve User by Email:**
    - A `findByEmail` method was added to the `UserRepository` interface.
    - A corresponding `getUserByEmail` method was added to the `UserService` to expose this functionality. This is used during the login process to fetch user details, including their role.

## File-by-File Breakdown

-   **`pom.xml`**: Added the MySQL connector dependency to enable communication with a MySQL database.
-   **`src/main/resources/application.properties`**: Updated database connection properties to point to the new MySQL database and commented out the old SQL Server connection details.
-   **`src/main/java/io/github/devtamakuwala/dailydine/controller/AuthController.java`**: Enhanced the login process to fetch the user's role and append it to the authentication token.
-   **`src/main/java/io/github/devtamakuwala/dailydine/repository/UserRepository.java`**: Added the `findByEmail(String email)` method definition.
-   **`src/main/java/io/github/devtamakuwala/dailydine/service/UserService.java`**: Implemented the `getUserByEmail(String email)` method to retrieve a user from the database using their email address.
