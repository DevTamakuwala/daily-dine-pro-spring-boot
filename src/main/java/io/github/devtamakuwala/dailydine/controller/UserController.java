package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
     * Get All users
     * */
    /**
     * Retrieves a list of all users in the system.
     * This is typically an admin-only function.
     *
     * @return A List of all User objects.
     */
    @GetMapping("users")
    public List<User> getAllUsers(Authentication authentication){
        return userService.getAllUsers();
    }

}
