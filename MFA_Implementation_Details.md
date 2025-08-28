# MFA Implementation Details

This document outlines the changes made to implement Time-based One-Time Password (TOTP) for multi-factor authentication (MFA) in the DailyDine application.

## Changes

### 1. Dependency Added

- The `totp-spring-boot-starter` dependency was added to `pom.xml` to provide the necessary library for TOTP functionality.

### 2. User Model Updated

- The `User` model (`User.java`) was updated to include two new fields:
    - `mfaEnabled`: A boolean flag to indicate whether MFA is enabled for the user.
    - `mfaSecret`: A string to store the user's MFA secret key.

### 3. MFA Controller

- A new controller, `MfaController.java`, was created to handle all MFA-related requests.
- It provides two endpoints:
    - `/api/mfa/setup`: To initiate the MFA setup process. This endpoint generates a new secret, and returns a QR code and a manual setup key.
    - `/api/mfa/verify`: To verify the TOTP and activate MFA for the user.

### 4. MFA Service

- A new service, `MfaService.java`, was created to encapsulate the business logic for MFA.
- This service is responsible for:
    - Generating new MFA secrets.
    - Creating QR code image data URIs.
    - Validating the TOTP codes provided by the user.

### 5. Configuration

- A commented-out line was added to `application.properties`. This should be removed before committing to production.
