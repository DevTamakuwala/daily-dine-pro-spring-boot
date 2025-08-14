# Documentation: Application Architecture and Auditing

This document provides a complete overview of the application, including its core components and the automatic auditing system. It serves as a single source of truth for understanding the project's architecture.

---

## Part 1: Core Application Components

Detailed Javadoc and inline comments were added to all existing controllers, services, and repositories to clarify their roles and functionality.

### Controllers
-   **`AuthController.java`**: Handles all public-facing authentication endpoints, including user login and registration with Firebase.
-   **`UserController.java`**: Manages user-specific data operations, such as retrieving user lists and handling profile updates.
-   **`HelloController.java`**: A utility controller for testing public and private endpoint security to ensure the authentication system is working correctly.

### Services
-   **`UserService.java`**: Contains the core business logic for user management, acting as a bridge between the `UserController` and the `UserRepository`.
-   **`FirebaseAuthService.java`**: Manages all direct communication with the Firebase Authentication REST API for user login and registration.
-   **`DecryptionService.java`**: A critical security component responsible for decrypting client-side encrypted data (like passwords) using an RSA private key before it is processed.

### Repositories
-   **`UserRepository.java`**: The JPA repository for the `User` entity. It provides all necessary CRUD (Create, Read, Update, Delete) operations for interacting with the user table in the database.

---

## Part 2: Automatic Auditing System

This section details the implementation and subsequent fixes of the automated auditing system.

### Initial Implementation
-   **`@EnableJpaAuditing`**: The feature was enabled in `JpaConfig.java` to activate Spring Data JPA's auditing capabilities.
-   **`AuditableEntity.java`**: An abstract base class was created to hold the four main auditing fields (`createdBy`, `createdAt`, `modifiedBy`, `modifiedAt`), preventing code duplication.
-   **`AuditorAwareImpl.java`**: A class was created to implement the `AuditorAware` interface, telling Spring who the current user is by getting their ID from the `SecurityContextHolder`.
-   **Entity Refactoring**: The `User`, `Customer`, and `Mess` entities were updated to extend `AuditableEntity`.

### Critical Fixes and Updates
1.  **Correction of User ID Type to `Integer`**:
    -   **Problem**: The system was incorrectly configured for `String` user IDs, while the application uses `Integer`.
    -   **Solution**: Updated `AuditorAwareImpl`, `AuditableEntity`, and `JpaConfig` to correctly use and handle `Integer` IDs, ensuring type safety.

2.  **Resolution of Server Startup Error**:
    -   **Problem**: A `BeanDefinitionOverrideException` occurred because `@EnableJpaAuditing` was present in two configuration files.
    -   **Solution**: The duplicate annotation was removed from `DailyDineApplication.java`, making `JpaConfig.java` the single source of truth for this configuration.

## Final Outcome

-   The application is now **fully documented**, clarifying the role of every component.
-   The auditing system is **fully operational**, correctly capturing `Integer` user IDs.
-   The application starts **reliably** without any configuration errors.
