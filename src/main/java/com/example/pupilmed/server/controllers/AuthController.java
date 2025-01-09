package com.example.pupilmed.server.controllers;

import com.example.pupilmed.server.models.AuthenticationResponse;
import com.example.pupilmed.server.security.auth.service.AuthService;
import com.example.pupilmed.server.security.jwt.JwtUtils;
import com.example.pupilmed.server.security.auth.service.CustomUserDetailsService;
import com.example.pupilmed.database.models.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private CustomUserDetailsService userDetailsService;
    private AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, CustomUserDetailsService userDetailsService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody User request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}