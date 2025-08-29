# MFA Backup Codes

This document outlines the changes made to implement backup codes for the Multi-Factor Authentication (MFA) feature in the DailyDine application.

## Changes

### 1. Backup Code Entity

- A new entity, `BackupCode.java`, was created to store hashed backup codes for users.
- This entity has a many-to-one relationship with the `User` entity.

### 2. User Entity Update

- The `User` entity was updated to include a one-to-many relationship with the `BackupCode` entity.
- This allows each user to have a list of associated backup codes.

### 3. MFA Service Update

- The `MfaService` was updated to include two new methods:
    - `generateBackupCodes()`: To generate a list of 10 random, 8-digit backup codes.
    - `verifyBackupCode()`: To verify a backup code provided by the user. This method also removes the backup code after it has been used.

### 4. MFA Controller Update

- The `MfaController` was updated to include the following changes:
    - The `/api/mfa/setup` endpoint now generates and returns a list of backup codes to the user.
    - The backup codes are hashed before being stored in the database.

### 5. Auth Controller Update

- The `AuthController` was updated to include a new endpoint:
    - `/api/auth/login-backup`: To allow users to log in with a backup code if they don't have access to their authenticator app.

### 6. New DTO

- A new Data Transfer Object (DTO), `BackupCodeLoginDTO.java`, was created to handle the request body for the backup code login endpoint.

### 7. New Repository

- A new repository, `BackupCodeRepository.java`, was created to handle database operations for the `BackupCode` entity.
