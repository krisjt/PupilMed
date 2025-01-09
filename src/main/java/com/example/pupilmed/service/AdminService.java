package com.example.pupilmed.service;

import com.example.pupilmed.repositories.OwnerRepository;
import com.example.pupilmed.repositories.VetRepository;
import com.example.pupilmed.repositories.VisitRepository;
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