package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.service.MessService;
import io.github.devtamakuwala.dailydine.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
     *
     */
    @GetMapping("/unverified")
    public ResponseEntity<?> getAllUnverifiedMess() {
        return messService.getAllUnverifiedMess();
    }


    /**
     * Get all Mess owners
     *
     */
    @GetMapping("")
    public ResponseEntity<?> getAllMess() {
        return new ResponseEntity<>(messService.getAllMess(), HttpStatus.OK);
    }


    /**
     * Get Mess by ID
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMess(@PathVariable("id") int id) {
        return userService.getUserByUserId(id);
    }


    /**
     * Update Mess Data
     *
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMessData(@PathVariable("id") int id, @RequestBody User user) {
        return messService.updateMess(id, user);
    }


    /**
     * This will approve the mess owner.
     * This is for admins
     *
     */
    @PutMapping(value = "approve/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> approveMess(@PathVariable("id") int id, @RequestBody Map<String, String> coordinates) {
        return messService.approveMess(id, coordinates);
    }

}
