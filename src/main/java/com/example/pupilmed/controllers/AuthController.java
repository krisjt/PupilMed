package com.example.pupilmed.controllers;

import com.example.pupilmed.models.database.user.User;
import com.example.pupilmed.models.server.AuthenticationResponse;
import com.example.pupilmed.security.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody User request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}