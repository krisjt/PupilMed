package com.example.pupilmed.server.service;

import com.example.pupilmed.database.models.owner.Owner;
import com.example.pupilmed.database.models.user.User;
import com.example.pupilmed.database.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository userRepository) {
        this.ownerRepository = userRepository;
    }

    public Owner getOwnerByUserID(User user) {
        return ownerRepository.findOwnerByUser(user);
    }
    public Owner getOwnerByUsername(String username) {
        return ownerRepository.findOwnerByUser_Username(username);
    }

}
