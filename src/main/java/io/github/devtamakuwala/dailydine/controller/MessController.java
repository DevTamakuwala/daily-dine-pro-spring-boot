package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.DTO.MessNearbyDTO;
import io.github.devtamakuwala.dailydine.DTO.MessUpdateDTO;
import io.github.devtamakuwala.dailydine.service.MessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private final MessService messService;

    public MessController(MessService messService) {
        this.messService = messService;
    }


    /**
     * Get all Verified Mess owners
     *
     */
    @GetMapping("/unverified")
    public ResponseEntity<?> getAllUnverifiedMess() {
        return messService.getAllUnverifiedMess();
    }

    /**
     * Get all Unverified Mess owners
     *
     */
    @GetMapping("/verified")
    public ResponseEntity<?> getAllVerifiedMess() {
        return messService.getAllVerifiedMess();
    }


    /**
     * Get all Mess owners
     *
     */
    @GetMapping("")
    public ResponseEntity<?> getAllMess() {
        return messService.getAllMess();
    }


    /**
     * Get Mess by ID
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMess(@PathVariable("id") int id) {
        return messService.getMessByMessId(id);
    }


    /**
     * Update Mess Data
     *
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMessData(@PathVariable("id") int id, @RequestBody MessUpdateDTO messUpdateDTO) {
        return messService.updateMess(id, messUpdateDTO);
    }


    /**
     * This will approve the mess owner.
     * This is for admins
     *
     */
    @PutMapping("approve/{id}")
    public ResponseEntity<?> approveMess(@PathVariable("id") int id, @RequestBody Map<String, String> coordinates) {
        return messService.approveMess(id, coordinates);
    }


    @GetMapping("/nearby")
    public ResponseEntity<?> getNearby(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double radius) {
        List<MessNearbyDTO> list = messService.getNearbyActiveMess(longitude, latitude, radius);
        radius *= 1000;
        if (list == null || list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No mess found within " + radius + " m. Please try changing location.");
        }
        return ResponseEntity.ok(list);
    }

}
