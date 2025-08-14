package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class contains the business logic for user-related operations.
 * It acts as an intermediary between the UserController and the UserRepository.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users from the database.
     * This method calls the repository to fetch all user records.
     *
     * @return A List of all User objects.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // TODO: Implement other user-related methods here, such as:
    // - createUser(User user)
    // - getUserById(Integer userId)
    // - updateUser(Integer userId, User userDetails)
    // - deleteUser(Integer userId)
}
