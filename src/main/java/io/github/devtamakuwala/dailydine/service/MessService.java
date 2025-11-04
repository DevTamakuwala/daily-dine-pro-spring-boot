package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.DTO.MessNearbyDTO;
import io.github.devtamakuwala.dailydine.DTO.UnverifiedMessOwnerDTO;
import io.github.devtamakuwala.dailydine.model.Mess;
import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.MessRepository;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing mess-related operations.
 */
@Service
public class MessService {

    private static final double EARTH_RADIUS = 6371000;
    final
    UserService userService;
    private final MessRepository repository;
    private final UserRepository userRepository;
    private final FirebaseAuthService authService;

    public MessService(UserService userService, MessRepository repository, UserRepository userRepository, FirebaseAuthService authService) {
        this.userService = userService;
        this.repository = repository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    /**
     * Creates or updates a mess in the database.
     * Evicts all entries from the "messes" and "unverifiedMess" caches to ensure data consistency.
     */
    @CacheEvict(value = {"messes", "unverifiedMess"}, allEntries = true)
    public void createMess(Mess mess) {
        repository.save(mess);
    }

    /**
     * Retrieves a list of all unverified mess owners.
     * The result is cached in the "unverifiedMess" cache.
     *
     * @return A ResponseEntity containing a list of unverified mess owners.
     */
    @Cacheable(value = "unverifiedMess", key = "'unverifiedMess'")
    public ResponseEntity<?> getAllUnverifiedMess() {
        List<UnverifiedMessOwnerDTO> unverifiedMessOwnerDTO = userRepository.findAllUnverifiedMessOwners();

        System.out.println(unverifiedMessOwnerDTO);
        return new ResponseEntity<>(unverifiedMessOwnerDTO, HttpStatus.OK);
    }

    /**
     * Retrieves a list of all verified mess owners.
     * The result is cached in the "verifiedMess" cache.
     *
     * @return A ResponseEntity containing a list of verified mess owners.
     */
    @Cacheable(value = "verifiedMess", key = "'verifiedMess'")
    public ResponseEntity<?> getAllVerifiedMess() {
        List<UnverifiedMessOwnerDTO> unverifiedMessOwnerDTO = userRepository.findAllVerifiedMessOwners();

        System.out.println(unverifiedMessOwnerDTO);
        return new ResponseEntity<>(unverifiedMessOwnerDTO, HttpStatus.OK);
    }

    /**
     * Retrieves a list of all users who are mess owners.
     * The result is cached in the "messes" cache with the key "'all'".
     */
    @Cacheable(value = "messes", key = "'all'")
    public ResponseEntity<?> getAllMess() {
        List<User> user = userRepository.findAllMess();
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Approves a mess account and sets its coordinates.
     * Evicts all entries from the "messes" and "unverifiedMess" caches to ensure data consistency.
     *
     * @return A ResponseEntity containing the updated user data.
     */
    @CacheEvict(value = {"messes", "unverifiedMess"}, allEntries = true)
    public ResponseEntity<?> approveMess(int id, Map<String, String> coordinates) {
        double latitude = Double.parseDouble(coordinates.get("latitude"));
        double longitude = Double.parseDouble(coordinates.get("longitude"));

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        // Activate the user and save the changes.
        user.setActive(true);
        userService.createUser(user);

        // Set the mess coordinates and save the changes.
        user.getMess().setLongitude(longitude);
        user.getMess().setLatitude(latitude);
        createMess(user.getMess());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    /**
     * Updates the data for a specific mess.
     * Evicts all entries from the "messes" and individual "mess" caches.
     *
     * @return A ResponseEntity containing the updated user data.
     */
    @CacheEvict(value = {"messes", "mess"}, allEntries = true)
    public ResponseEntity<?> updateMess(int id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return new ResponseEntity<>("User with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        // Update the user fields from the request data.
        existingUser.setPhoneNo(user.getPhoneNo());
        existingUser.setActive(user.isActive());

        userService.createUser(existingUser);

        // Update the associated mess details.
        existingUser.getMess().setMessPhoneNo(user.getMess().getMessPhoneNo());
        existingUser.getMess().setCity(user.getMess().getCity());
        existingUser.getMess().setState(user.getMess().getState());
        existingUser.getMess().setZipCode(user.getMess().getZipCode());
        existingUser.getMess().setAddress(user.getMess().getAddress());
        existingUser.getMess().setLatitude(user.getMess().getLatitude());
        existingUser.getMess().setLongitude(user.getMess().getLongitude());
        createMess(existingUser.getMess());
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    /**
     * Retrieves a mess by its ID and caches the result.
     * The result is cached in the "mess" cache with the key being the mess ID.
     */
    @Cacheable(value = "mess", key = "#id")
    public ResponseEntity<?> getMessByMessId(int id) {
        Mess mess = repository.findById(id).orElse(null);
        if (mess != null) {
            return ResponseEntity.ok(mess);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    public ResponseEntity<?> updatePassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.isActive()) {
            return new ResponseEntity<>(authService.sendPasswordResetEmail(user.getEmail()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
    }
}
