package io.github.devtamakuwala.dailydine.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * This service provides functionality to decrypt data using an RSA private key.
 * It is specifically used to decrypt passwords sent from the client-side before they are
 * used for authentication with Firebase.
 */
@Service
public class DecryptionService {

    /**
     * Decrypts a Base64 encoded string using the RSA private key.
     *
     * @param base64Encrypted The Base64 encoded, RSA-encrypted string (e.g., a password).
     * @return The decrypted string in plain text.
     * @throws Exception if the private key cannot be loaded or if the decryption fails.
     */
    public static String decryptPassword(String base64Encrypted) throws Exception {
        // Load the private key from the specified file in the classpath.
        PrivateKey privateKey = loadPrivateKey("keys/private.pem");

        // Get a Cipher instance for RSA encryption/decryption.
        Cipher cipher = Cipher.getInstance("RSA");
        // Initialize the cipher for decryption mode with the loaded private key.
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Decode the Base64 input string into its raw byte representation.
        byte[] encryptedBytes = Base64.getDecoder().decode(base64Encrypted);
        // Perform the final decryption.
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert the decrypted bytes back to a string and return it.
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Loads an RSA private key from a PEM file located in the application's classpath.
     *
     * @param path The path to the PEM file within the classpath (e.g., "keys/private.pem").
     * @return A PrivateKey object.
     * @throws Exception if the key file cannot be read or if the key specification is invalid.
     */
    private static PrivateKey loadPrivateKey(String path) throws Exception {
        // Use Spring's ClassPathResource to load the key file from the classpath.
        Resource resource = new ClassPathResource(path);
        String privateKeyPEM = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // The PEM file contains header and footer lines that need to be removed
        // before the key can be parsed.
        privateKeyPEM = privateKeyPEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""); // Remove all whitespace and newlines.

        // Decode the cleaned Base64 string into key bytes.
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);

        // Create a key specification from the raw key bytes.
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        // Get a KeyFactory instance for RSA.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // Generate and return the PrivateKey object.
        return keyFactory.generatePrivate(keySpec);
    }
}
