package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.server.OwnerResponse;
import com.example.pupilmed.repositories.OwnerRepository;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private OwnerRepository ownerRepository;
    private JwtUtils jwtUtils;

    @Autowired
    public OwnerService(OwnerRepository userRepository, JwtUtils jwtUtils) {
        this.ownerRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public Owner getOwnerByUserID(User user) {
        return ownerRepository.findOwnerByUser(user);
    }
    public Owner getOwnerByUsername(String user) {
        return ownerRepository.findOwnerByUser_Username(user);
    }
    public Owner getOwnerByUser(User user){
        return ownerRepository.getOwnerByUser(user);
    }

    public ResponseEntity<OwnerResponse> getAccount(String authHeader){
        if(authHeader == null){
            return new ResponseEntity<>(new OwnerResponse(null,null, null), HttpStatus.FORBIDDEN);
        }
        String username = jwtUtils.getUsernameFromHeader(authHeader);

        Owner owner = getOwnerByUsername(username);

        if(owner == null){
            return new ResponseEntity<>(new OwnerResponse(null,null, null), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new OwnerResponse(owner.getName(),owner.getSurname(),username), HttpStatus.OK);
    }
}
