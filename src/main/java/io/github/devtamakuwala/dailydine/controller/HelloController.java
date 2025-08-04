package io.github.devtamakuwala.dailydine.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from a public endpoint! You don't need to be authenticated.";
    }

    @GetMapping("/private/hello")
    public String privateHello(Authentication authentication) {
        // The 'principal' is the user's UID from the Firebase token
        String userId = authentication.getName();
        return "Hello, " + userId + "! You are accessing a private endpoint.";
    }
}
