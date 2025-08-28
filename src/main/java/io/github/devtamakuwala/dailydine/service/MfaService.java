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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * Service for handling Multi-Factor Authentication (MFA) logic.
 * This service provides methods for generating secrets, creating QR codes, and validating OTPs.
 */
@Service
@Slf4j
public class MfaService {

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
