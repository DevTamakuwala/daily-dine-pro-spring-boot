package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.service.MessService;
import io.github.devtamakuwala.dailydine.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This controller handles all mess-related endpoints.
 * It provides functionality for administrators to manage mess accounts, such as approving new messes
 * and updating existing ones.
 */
@Slf4j
@RestController
@RequestMapping("/api/mess/")
// Enables Cross-Origin Resource Sharing (CORS) for all endpoints in this controller,
// allowing requests from any origin. This is useful for development with separate frontends.
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS})
public class MessController {

    @Autowired
    private MessService messService;
    @Autowired
    private UserService userService;


    /**
     * Get all Unverified Mess owners
     * */
    /**
     * Retrieves a list of all unverified mess owners.
     * This endpoint is intended for administrators to review and approve new mess accounts.
     *
     * @param authentication The authentication principal, used to ensure the user is authorized.
     * @return A ResponseEntity containing a list of unverified mess owners, or an error message if the user is not authenticated.
     */
    @GetMapping("/unverified")
    public ResponseEntity<?> getAllUnverifiedMess(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(messService.getAllUnverifiedMess(), HttpStatus.OK);
    }


    /**
     * Get all Mess owners
     * */
    /**
     * Retrieves a list of all messes.
     * @param authentication The authentication principal, used to ensure the user is authorized.
     * @return A ResponseEntity containing a list of all messes.
     */
    @GetMapping("")
    public ResponseEntity<?> getAllMess(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(messService.getAllMess(), HttpStatus.OK);
    }


    /**
     * Get Mess by ID
     * */
    /**
     * Retrieves a single mess by its user ID.
     * @param authentication The authentication principal, used to ensure the user is authorized.
     * @param id The ID of the user associated with the mess.
     * @return A ResponseEntity containing the user and their mess details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMess(Authentication authentication, @PathVariable("id") int id) {
        User user = userService.getUserByUserId(id);

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    /**
     * Update Mess Data
     * */
    /**
     * Updates the data for a specific mess.
     * Note: This endpoint accepts the full User object, which is not ideal for security and maintainability.
     * A better approach is to use a specific Data Transfer Object (DTO) for the request body
     * to control exactly which fields can be updated.
     *
     * @param authentication The authentication principal, used to ensure the user is authorized.
     * @param id The ID of the user to update.
     * @param user The User object containing the new data.
     * @return A ResponseEntity containing the updated user data.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMessData(Authentication authentication, @PathVariable("id") int id, @RequestBody User user) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        User existingUser = userService.getUserByUserId(id);

        if (existingUser == null) {
            return new ResponseEntity<>("User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        // Update the user fields from the request data.
        existingUser.setPhoneNo(user.getPhoneNo());
        existingUser.setActive(user.isActive());

        // The service method should ideally be named 'updateUser' or 'saveUser'.
        // Assuming 'createUser' internally uses 'save', which handles updates if the entity has an ID.
        userService.createUser(existingUser);

        // Update the associated mess details.
        existingUser.getMess().setMessPhoneNo(user.getMess().getMessPhoneNo());
        existingUser.getMess().setCity(user.getMess().getCity());
        existingUser.getMess().setState(user.getMess().getState());
        existingUser.getMess().setZipCode(user.getMess().getZipCode());
        existingUser.getMess().setAddress(user.getMess().getAddress());
        existingUser.getMess().setLatitude(user.getMess().getLatitude());
        existingUser.getMess().setLongitude(user.getMess().getLongitude());
        // This call might be redundant if CascadeType.ALL is correctly configured on the User entity.
        messService.createMess(existingUser.getMess());
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    /**
     * Approves a mess account and sets its coordinates.
     * This endpoint is used by administrators to mark a mess as verified and active.
     *
     * @param authentication The authentication principal, used to ensure the user is authorized.
     * @param id The ID of the user to approve.
     * @param coordinates A map containing the latitude and longitude of the mess.
     * @return A ResponseEntity containing the updated user data.
     */
    @PutMapping(value = "approve/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> approveMess(Authentication authentication, @PathVariable("id") int id, @RequestBody Map<String, String> coordinates) {

        String latitude = coordinates.get("latitude");
        String longitude = coordinates.get("longitude");

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByUserId(id);

        if (user == null) {
            return new ResponseEntity<>("User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        // Activate the user and save the changes.
        user.setActive(true);
        userService.createUser(user);

        // Set the mess coordinates and save the changes.
        // This second save might be redundant if cascading is working as expected.
        user.getMess().setLongitude(longitude);
        user.getMess().setLatitude(latitude);
        messService.createMess(user.getMess());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
