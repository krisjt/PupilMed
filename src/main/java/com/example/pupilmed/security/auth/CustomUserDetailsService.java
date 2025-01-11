package com.example.pupilmed.security.auth;

import com.example.pupilmed.repositories.OwnerRepository;
import com.example.pupilmed.repositories.VetRepository;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private VetRepository vetRepository;
    private OwnerRepository ownerRepository;



    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, VetRepository vetRepository, OwnerRepository ownerRepository) {
        this.userRepository = userRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.isActive()) {
                throw new DisabledException("User is not active");
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().toString())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
