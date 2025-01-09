package com.example.pupilmed.server.security.auth.service;

import com.example.pupilmed.server.models.AuthenticationResponse;
import com.example.pupilmed.server.security.jwt.JwtUtils;
import com.example.pupilmed.database.models.user.Role;
import com.example.pupilmed.database.models.user.User;
import com.example.pupilmed.database.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;


    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse authenticate(User requestUser){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword()));

        User user = repository.findByUsername(requestUser.getUsername()).orElseThrow();

        String token = jwtUtils.generateToken(user);
        Role role = user.getRole();

        return  new AuthenticationResponse(token, role);
    }
}
