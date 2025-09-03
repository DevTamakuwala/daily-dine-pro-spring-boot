package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Retrieves all users from the database.
     * This method delegates the call to the repository to fetch all user records.
     *
     * @return A List of all User objects.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates or updates a user in the database.
     * The JpaRepository's save() method handles both the creation of new users
     * and the update of existing ones if an ID is present.
     * @param user The User entity to be saved.
     */
    public void createUser(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves a user by their email address.
     * This method delegates the call to the custom findByEmail method in the UserRepository.
     * @param email The email of the user to find.
     * @return The User object if found, otherwise null.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by their unique ID.
     * The orElse(null) part ensures that if no user is found, null is returned,
     * which can be handled by the calling code.
     * @param id The ID of the user to find.
     * @return The User object if found, otherwise null.
     */
    public User getUserByUserId(int id) {
        return userRepository.findById(id).orElse(null);
    }

}
