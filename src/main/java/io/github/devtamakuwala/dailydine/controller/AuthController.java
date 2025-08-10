package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.DTO.LoginDTO;
import io.github.devtamakuwala.dailydine.service.DecryptionService;
import io.github.devtamakuwala.dailydine.service.FirebaseAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDTO login) {

        try {
            login.setPassword(DecryptionService.decryptPassword(login.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<String> idToken;
        try {
            idToken = new ResponseEntity<>(firebaseAuthService.loginAndGetIdToken(login.getEmail(), login.getPassword()), HttpStatus.FOUND);
//            idToken = firebaseAuthService.loginAndGetIdToken(username, password);
        } catch (Exception e) {

//            log.error(String.valueOf(e.getMessage()));
            idToken = new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//            idToken = e.getMessage();
        }

        return idToken;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody LoginDTO register) {
        try {
            register.setPassword(DecryptionService.decryptPassword(register.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<String> idToken;
        try {
            idToken = new ResponseEntity<>(firebaseAuthService.registerUser(register.getEmail(), register.getPassword()), HttpStatus.CREATED);
        } catch (Exception e) {
            idToken = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return idToken;
    }

}
