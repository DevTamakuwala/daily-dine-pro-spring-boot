package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.service.UserService;
import org.springframework.http.HttpStatus;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Get All users
     *
     */
    @GetMapping("users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Get one user
     * */
    @GetMapping("user/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return userService.getUser(id);
    }


    /**
     * Update user data
     * */
    @PutMapping("user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

}
