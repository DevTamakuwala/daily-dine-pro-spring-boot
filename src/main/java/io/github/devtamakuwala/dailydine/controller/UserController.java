package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles all user-related operations, such as retrieving user lists
 * and managing user profiles. It is distinct from AuthController, which only handles authentication.
 */
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves a list of all users in the system.
     * This is typically an admin-only function.
     *
     * @return A List of all User objects.
     */
    @GetMapping("users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Updates the profile of a specific user.
     * // TODO: This endpoint is not yet implemented.
     * // It should validate the input, call the UserService to perform the update,
     * // and return a proper ResponseEntity with the updated user or an error status.
     *
     * @param userId The ID of the user to be updated.
     * @param user   A User object containing the new data for the user.
     * @return A ResponseEntity containing the updated User object or an error status.
     */
    @PutMapping("users/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user){
        // The business logic for updating the user should be handled in the UserService.
        // For example: return ResponseEntity.ok(userService.updateUser(userId, user));
        return null;
    }
}
