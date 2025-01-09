package com.example.pupilmed.server.service;

import com.example.pupilmed.database.repositories.OwnerRepository;
import com.example.pupilmed.database.repositories.VetRepository;
import com.example.pupilmed.database.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private OwnerRepository ownerRepository;
    private VisitRepository visitRepository;
    private VetRepository vetRepository;

    @Autowired
    public AdminService(OwnerRepository userRepository, VisitRepository visitRepository, VetRepository vetRepository) {
        this.ownerRepository = userRepository;
        this.visitRepository = visitRepository;
        this.vetRepository = vetRepository;
    }



}