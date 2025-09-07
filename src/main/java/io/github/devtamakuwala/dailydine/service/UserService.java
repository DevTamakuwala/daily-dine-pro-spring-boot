package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BackupCodeService backupCodeService;

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
    public User getUserByUserId(int id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Disables the MFA by the user
     * */
    @Transactional
    public void disableMfa(User user) {
        backupCodeService.removeBackupCodeByUserId(user.getUserId());

        user.setMfaEnabled(false);
        user.setMfaSecret(null);
        userRepository.save(user);
    }

}
