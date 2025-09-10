package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.http.HttpStatus;
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
     * Retrieves all users from the database.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates or updates a user in the database.
     */
    public void createUser(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves a user by their email address.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by their unique ID.s
     */
    public ResponseEntity<?> getUserByUserId(int id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Disables the MFA by the user
     *
     */
    @Transactional
    public void disableMfa(User user) {
        backupCodeService.removeBackupCodeByUserId(user.getUserId());

        user.setMfaEnabled(false);
        user.setMfaSecret(null);
        userRepository.save(user);
    }

}
