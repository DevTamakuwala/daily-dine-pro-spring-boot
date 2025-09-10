package io.github.devtamakuwala.dailydine.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import io.github.devtamakuwala.dailydine.DTO.VerifyRequestDTO;
import io.github.devtamakuwala.dailydine.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * Service for handling Multi-Factor Authentication (MFA) logic.
 * This service provides methods for generating secrets, creating QR codes, and validating OTPs.
 */
@Service
@Slf4j
public class MfaService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService;

    public MfaService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Generates a new random 32-character secret key for MFA.
     *
     */
    public String generateNewSecret() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }

    /**
     * Generates a QR code image data URI for the user to scan with their authenticator app.
     */
    public String generateQrCodeImageUri(String secret, String email) {
        QrData data = new QrData.Builder()
                .label(email)
                .secret(secret)
                .issuer("Daily Dine")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];
        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
            log.error("Error while generating QR code: {}", e.getMessage());
        }

        return getDataUriForImage(imageData, generator.getImageMimeType());
    }

    /**
     * Generates a list of 10 random, 10-digit backup codes for MFA.
     */
    public List<String> generateBackupCodes() {
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 10)
                .mapToObj(i -> {
                    // Generate a 10-digit code (from 0 to 9,999,999,999)
                    long code = Math.abs(random.nextLong() % 10_000_000_000L);
                    // Format the number as a 10-digit string, padded with leading zeros
                    return String.format("%010d", code);
                })
                .collect(Collectors.toList());
    }

    /**
     * Verifies a backup code provided by the user.
     */
    public boolean verifyBackupCode(User user, String code) {
        // Find a stored code that matches the provided code
        return user.getBackupCodes().stream()
                .filter(backupCode -> passwordEncoder.matches(code, backupCode.getCode()))
                .findFirst()
                .map(backupCode -> {
                    // If a match is found, remove it so it can't be used again
                    user.getBackupCodes().remove(backupCode);
                    return true;
                }).orElse(false);
    }

    /**
     * Verifies the 6-digit Time-based One-Time Password (TOTP) provided by the user.
     */
    public boolean isOtpValid(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }


    /**
     * This will set up the MFA for one user
     *
     * @param body will be taken and using that the MFA will be set up
     *             in this param there will be email of the user
     * @return QR code link, manual setup key and backup codes
     *
     */
    public ResponseEntity<?> setUpMfa(Map<String, String> body) {
        User user = userService.getUserByEmail(body.get("email"));
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (user.isMfaEnabled()) {
            return ResponseEntity.badRequest().body("MFA is already enabled for this account. Please disable it first if you wish to reset.");
        }
        // Clear any old codes before generating new ones
        user.getBackupCodes().clear();

        final String secret = generateNewSecret();
        final List<String> backupCodes = generateBackupCodes();

        // Hash the codes before saving them to the database
        backupCodes.forEach(code -> user.getBackupCodes().add(new io.github.devtamakuwala.dailydine.model.BackupCode(passwordEncoder.encode(code), user)));

        user.setMfaSecret(secret);
        user.setMfaEnabled(false); // MFA is not yet active until verified
        userService.createUser(user);

        // This is where you provide both the QR code and the manual setup key
        // Return the PLAIN TEXT codes to the user ONE TIME
        return ResponseEntity.ok(Map.of(
                "qrCodeUri", generateQrCodeImageUri(secret, user.getEmail()),
                "manualSetupKey", secret,
                "backupCodes", backupCodes // Add this to the response
        ));
    }

    /**
     * Service method to verify the Time-based One-Time Password (TOTP) and activate MFA.
     *
     * @param verifyRequest The request body containing the user's email and the verification code.
     * @return A response entity with a success or error message.
     */
    public ResponseEntity<?> verifyCode(VerifyRequestDTO verifyRequest) {
        User user = userService.getUserByEmail(verifyRequest.email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (isOtpValid(user.getMfaSecret(), verifyRequest.code)) {
            user.setMfaEnabled(true);
            userService.createUser(user);
            return ResponseEntity.ok("MFA has been enabled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Incorrect code. Please try again.");
        }
    }

    /**
     * This will fetch the user and delete it's MFA from the user table as well as the backup codes
     *
     */
    public ResponseEntity<?> deleteMfa(VerifyRequestDTO verifyRequest) {
        User user = userService.getUserByEmail(verifyRequest.email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (isOtpValid(user.getMfaSecret(), verifyRequest.code)) {
            // Call the single transactional method
            userService.disableMfa(user);
            return ResponseEntity.ok("MFA has been successfully disabled.");
        } else {
            return ResponseEntity.badRequest().body("Incorrect code. Please try again.");
        }
    }
}
