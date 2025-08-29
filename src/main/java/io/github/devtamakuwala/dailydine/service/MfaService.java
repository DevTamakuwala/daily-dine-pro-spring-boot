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
     *
     * @return A new MFA secret as a string.
     */
    public String generateNewSecret() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }

    /**
     * Generates a QR code image data URI for the user to scan with their authenticator app.
     *
     * @param secret The MFA secret.
     * @param email The user's email address.
     * @return A data URI for the QR code image.
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
            log.error("Error while generating QR code: " + e.getMessage());
        }

        return getDataUriForImage(imageData, generator.getImageMimeType());
    }

    /**
     * Generates a list of 10 random, 10-digit backup codes for MFA.
     *
     * @return A list of 10 backup codes.
     */
    public List<String> generateBackupCodes() {
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 10)
                .mapToObj(i -> {
                    // Generate an 10-digit code (from 0 to 99,999,999)
                    int code = random.nextInt(100_000_000);
                    String codeStr = String.format("%10d", code); // Pad with leading zeros if needed

                    // Format the code for readability
                    return codeStr.substring(0, 5) + "-" + codeStr.substring(5);
                })
                .collect(Collectors.toList());
    }

    /**
     * Verifies a backup code provided by the user.
     *
     * @param user The user attempting to log in.
     * @param code The backup code to verify.
     * @return True if the code is valid and has not been used before, false otherwise.
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
     *
     * @param secret The user's MFA secret.
     * @param code The 6-digit code to verify.
     * @return True if the code is valid, false otherwise.
     */
    public boolean isOtpValid(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }
}
