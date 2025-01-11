package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Role;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.repositories.UserRepository;
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
