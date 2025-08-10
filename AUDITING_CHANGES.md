# Feature: Automatic Entity Auditing

This update introduces a robust and automated auditing system to the application using Spring Data JPA. This system automatically tracks when an entity is created or modified, and by whom, without requiring manual code in the business logic.

## Summary of Changes

### 1. Core Auditing Infrastructure

-   **`@EnableJpaAuditing`**: The auditing feature was enabled in `JpaConfig.java`. This annotation is the master switch that activates Spring Data JPA's auditing capabilities.

-   **`AuditableEntity.java`**: A new abstract class was created to serve as a base for all entities that require auditing.
    -   It uses the `@MappedSuperclass` annotation, so its fields are inherited by subclasses without it being a separate database table.
    -   It contains the following fields with corresponding JPA annotations:
        -   `createdBy` (`@CreatedBy`): Stores the ID of the user who created the entity.
        -   `createdAt` (`@CreatedDate`): Stores the timestamp of when the entity was created.
        -   `modifiedBy` (`@LastModifiedBy`): Stores the ID of the user who last modified the entity.
        -   `modifiedAt` (`@LastModifiedDate`): Stores the timestamp of the last modification.

-   **`AuditorAwareImpl.java`**: This class implements the `AuditorAware<String>` interface.
    -   Its primary responsibility is to provide the ID of the currently authenticated user to the auditing framework.
    -   It uses `SecurityContextHolder` from Spring Security to safely access the current user's principal and return their username (or ID). This is how the `createdBy` and `modifiedBy` fields are populated automatically.

-   **`JpaConfig.java`**: This configuration file was updated to register the `AuditorAwareImpl` as a Spring bean, making it available to the JPA auditing mechanism.

### 2. Entity Refactoring

The following entities were updated to integrate with the new auditing system:

-   **`User.java`**
-   **`Customer.java`**
-   **`Mess.java`**

The changes for each were:
-   They now `extend AuditableEntity`.
-   Redundant, manually-managed audit fields (like `createdDate`, `lastModifiedBy`, etc.) were removed from the `User` entity to rely solely on the inherited, automatically managed fields.

### 3. Code Documentation

-   **Javadoc and Inline Comments**: Detailed comments were added to all the new and modified files. The comments explain the purpose of each class, method, and annotation to ensure the new auditing system is easy to understand and maintain.

## How It Works

1.  When a repository's `save()` method is called for an entity that extends `AuditableEntity`:
2.  Spring's auditing aspect intercepts the call.
3.  It invokes our `AuditorAwareImpl` to get the current user's ID.
4.  It populates the `@CreatedBy`, `@LastModifiedBy`, `@CreatedDate`, and `@LastModifiedDate` fields automatically just before persisting the data to the database.

This new system enhances data integrity and provides a clear, automated audit trail for key entities in the application.
