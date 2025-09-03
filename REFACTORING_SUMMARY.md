# Refactoring and Bug Fix Summary

This document summarizes the critical refactoring and bug fixes applied to the DailyDine backend application. The primary goals of these changes were to resolve persistent database errors, fix JSON serialization issues, and improve the overall stability, maintainability, and adherence to best practices.

## 1. Critical JPA Entity Refactoring

The most significant change was the refactoring of all JPA entities (`User`, `Customer`, `Mess`, `BackupCode`) to resolve a `TransientObjectException`.

**Problem:**
- The use of Lombok's `@Data` annotation on JPA entities was causing a severe issue. `@Data` generates `equals()` and `hashCode()` methods based on all fields, which is incorrect for entities with relationships. This corrupted the Hibernate persistence context, leading to unpredictable errors.

**Solution:**
- **Removed `@Data`:** The `@Data` annotation was removed from all entities.
- **Replaced with Specific Annotations:** It was replaced with `@Getter`, `@Setter`, `@AllArgsConstructor`, `@NoArgsConstructor`, and a safe `@ToString(exclude = "...")` to prevent infinite loops in logging.
- **Correct `equals()` and `hashCode()`:** Each entity now has a manually implemented `equals()` and `hashCode()` method that relies *only* on the primary key (`userId`, `customerId`, etc.). This is a critical best practice for JPA entities and ensures the stability of the persistence layer.

## 2. JSON Serialization and Deserialization Fixes

Several issues related to JSON processing were resolved, which were causing `HttpMediaTypeNotSupportedException` and `InvalidDefinitionException` errors.

**Problem:**
- Bidirectional relationships between entities (e.g., `User` and `Customer`) were not correctly configured for Jackson, the JSON library. This caused infinite loops during serialization.

**Solution:**
- **Named JSON References:** The `@JsonManagedReference` and `@JsonBackReference` annotations were updated to include specific names (e.g., `@JsonManagedReference("user-customer")`). This allows Jackson to correctly reconstruct the object graph without getting stuck in loops.
- **Added `JacksonConfig`:** A new configuration file (`JacksonConfig.java`) was added to ensure that Java 8 date and time objects are serialized into a standard, human-readable format (ISO-8601) instead of numeric timestamps.

## 3. Code Clarity and Consistency

Several changes were made to improve the clarity and consistency of the codebase.

**Problem:**
- Some entities (`Customer`, `Mess`) had a field named `userId` that was actually a full `User` object. This was confusing and inconsistent.

**Solution:**
- **Renamed Fields:** The `userId` fields in the `Customer` and `Mess` entities were renamed to `user` to more accurately reflect their type.
- **Updated Mappings:** The `mappedBy` attribute in the `@OneToOne` annotations in the `User` entity was updated to reflect this change.
- **Updated Controller Logic:** The `AuthController` was updated to use the new `setUser()` method.

## 4. Comprehensive Code Documentation

**Problem:**
- Many parts of the codebase lacked comments, making it difficult to understand the purpose of the code and the rationale behind certain design decisions.

**Solution:**
- **Added Detailed Comments:** All modified files, including entities, controllers, services, and repositories, have been thoroughly commented. The comments explain the purpose of each class and method, clarify complex annotations (`@EntityGraph`, `@JsonBackReference`), and document the refactoring changes.
