package com.example.pupilmed.service;

import com.example.pupilmed.models.database.owner.Owner;
import com.example.pupilmed.models.database.user.User;
import com.example.pupilmed.models.database.vet.Vet;
import com.example.pupilmed.repositories.OwnerRepository;
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
    public Owner getOwnerByUsername(String user) {
        return ownerRepository.findOwnerByUser_Username(user);
    }
    public Owner getOwnerByUser(User user){
        return ownerRepository.getOwnerByUser(user);
    }
}
