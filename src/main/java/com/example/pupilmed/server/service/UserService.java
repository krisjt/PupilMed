package com.example.pupilmed.server.service;

import com.example.pupilmed.database.models.user.Role;
import com.example.pupilmed.database.models.user.User;
import com.example.pupilmed.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public boolean existsByUsernameAndRole(String phoneNumber, Role role) {
        return userRepository.existsByUsernameAndRole(phoneNumber,role);
    }

}
