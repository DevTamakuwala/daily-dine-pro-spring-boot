package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.Mess;
import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.MessRepository;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service class for managing mess-related operations.
 */
@Service
public class MessService {

    final
    UserService userService;
    private final MessRepository repository;
    private final UserRepository userRepository;

    public MessService(UserService userService, MessRepository repository, UserRepository userRepository) {
        this.userService = userService;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    /**
     * Creates or updates a mess in the database.
     */
    public void createMess(Mess mess) {
        repository.save(mess);
    }

    /**
     * Retrieves a list of all unverified mess owners.

     * Retrieves a list of all unverified mess owners.
     * This endpoint is intended for administrators to review and approve new mess accounts.
     *
     * @return A ResponseEntity containing a list of unverified mess owners, or an error message if the user is not authenticated.
     */
    public ResponseEntity<?> getAllUnverifiedMess() {
        return new ResponseEntity<>(userRepository.findAllUnverifiedMessOwners(), HttpStatus.OK);
    }

    /**
     * Retrieves a list of all users who are mess owners.
     */
    public ResponseEntity<?> getAllMess() {
        return new ResponseEntity<>(userRepository.findAllMess(), HttpStatus.OK);
    }

    /**
     * Approves a mess account and sets its coordinates.
     * This endpoint is used by administrators to mark a mess as verified and active.
     *
     * @return A ResponseEntity containing the updated user data.
     */
    public ResponseEntity<?> approveMess(int id, Map<String, String> coordinates) {
        String latitude = coordinates.get("latitude");
        String longitude = coordinates.get("longitude");

        User user = userRepository.findById(id).orElse(null);

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
        createMess(user.getMess());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    /**
     * Updates the data for a specific mess.
     * Note: This endpoint accepts the full User object, which is not ideal for security and maintainability.
     * A better approach is to use a specific Data Transfer Object (DTO) for the request body
     * to control exactly which fields can be updated.
     *
     * @return A ResponseEntity containing the updated user data.
     */
    public ResponseEntity<?> updateMess(int id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);

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
        createMess(existingUser.getMess());
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }
}
