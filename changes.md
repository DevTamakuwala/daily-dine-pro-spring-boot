# Bug Fix: Resolved `TransientObjectException` During User Registration

## Problem

A `org.hibernate.TransientObjectException` was occurring when a new user was registered. This was because the `User` entity was being saved before its associated `Customer` and `Mess` entities, which are required to be persisted first.

## Solution

The fix was implemented by leveraging cascading persistence, which simplifies the code and ensures that related entities are saved in the correct order. Here’s what was done:

1.  **Enabled Cascading in `User.java`**:
    *   The `@OneToOne` mappings for both the `customer` and `mess` fields were updated with `cascade = CascadeType.ALL`.
    *   This change instructs Hibernate to automatically save the `Customer` and `Mess` entities whenever a `User` is saved, eliminating the need for manual persistence.

2.  **Simplified `AuthController.java`**:
    *   The explicit calls to `customerService.createCustomer` and `messService.createMess` were removed from the `register` method.
    *   With cascading enabled, these calls are no longer necessary, as the database now handles the saving of associated entities automatically.

By making these changes, the `TransientObjectException` is resolved, and the user registration process is now more robust and efficient.