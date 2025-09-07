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
import io.github.devtamakuwala.dailydine.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
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

    /**
     * Generates a new random 32-character secret key for MFA.
     * */
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
}
