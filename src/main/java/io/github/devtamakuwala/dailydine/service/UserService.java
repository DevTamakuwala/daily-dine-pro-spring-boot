package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service class contains the business logic for user-related operations.
 * It acts as an intermediary between the controllers and the UserRepository, encapsulating
 * all interactions with the user data store.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BackupCodeService backupCodeService;

    public UserService(UserRepository userRepository, BackupCodeService backupCodeService) {
        this.userRepository = userRepository;
        this.backupCodeService = backupCodeService;
    }

    /**
     * Retrieves all users from the database and caches the result.
     * The result is cached in the "users" cache with the key "'all'".
     */
    @Cacheable(value = "users", key = "'all'")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates or updates a user in the database.
     * Evicts the "users" cache and the specific user's cache entries by both ID and email.
     */
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#user.email"),
            @CacheEvict(value = "user", key = "#user.userId")
    })
    public void createUser(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves a user by their email address and caches the result.
     * The result is cached in the "user" cache with the key being the email.
     */
    @Cacheable(value = "user", key = "#email")
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Disables two-factor authentication for a user.
     * Evicts the "users" cache and the specific user's cache entries by both ID and email.
     */
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#user.email"),
            @CacheEvict(value = "user", key = "#user.userId")
    })
    @Transactional
    public void disableMfa(User user) {
        backupCodeService.removeBackupCodeByUserId(user.getUserId());

        user.setMfaEnabled(false);
        user.setMfaSecret(null);
        userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID and caches the result.
     * The result is cached in the "user" cache with the key being the user ID.
     */
    @Cacheable(value = "user", key = "#id")
    public ResponseEntity<?> getUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
}
