# Admin Features and Optimizations

This document outlines the recent changes made to the DailyDine application, which include security enhancements, performance optimizations, and new features for administrators.

## Changes

### 1. Security Enhancements

- The `SecurityConfig.java` file was updated to enforce authentication for all API endpoints under `/api/**`, except for the public and authentication-related endpoints.

### 2. Authentication Flow

- The `AuthController.java` file was updated to return a map of user information upon successful login, including the user's role, visibility, and MFA status.
- The `MfaController.java` file was updated to include the `Authentication` principal in the `setupDevice` and `verifyCode` methods, ensuring that only authenticated users can access these endpoints.

### 3. Admin Features

- A new controller, `MessController.java`, was created to provide functionality for administrators to manage mess accounts.
- A new endpoint, `/api/mess/`, was added to retrieve a list of all unverified mess owners.
- A new Data Transfer Object (DTO), `UnverifiedMessOwnerDTO.java`, was created to send a simplified view of the user to the client.
- The `UserRepository.java` file was updated with a new query to find all unverified mess owners.
- The `MessService.java` file was updated with a new method to retrieve the list of unverified mess owners.

### 4. Performance Optimizations

- The `User.java`, `Customer.java`, and `Mess.java` models were updated to use lazy loading (`FetchType.LAZY`) for their relationships. This optimizes performance by only loading the associated objects from the database when they are explicitly accessed.
- The `UserRepository.java` file was updated to use an entity graph (`@EntityGraph`) to eagerly fetch the associated `mess` entity when retrieving unverified mess owners, preventing the N+1 problem.
- The `User.java` model was updated to include `@JsonManagedReference` on the `mess`, `customer`, and `backupCodes` fields to prevent infinite recursion during JSON serialization.
