package com.example.pupilmed.service;

import com.example.pupilmed.models.database.owner.Owner;
import com.example.pupilmed.models.database.pet.Pet;
import com.example.pupilmed.repositories.PetRepository;
import com.example.pupilmed.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private PetRepository petRepository;
    private OwnerService ownerService;
    private JwtUtils jwtUtils;

    @Autowired
    public PetService(PetRepository petRepository, OwnerService ownerService, JwtUtils jwtUtils) {
        this.petRepository = petRepository;
        this.ownerService = ownerService;
        this.jwtUtils = jwtUtils;
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwnerID(int id) {
        return petRepository.findPetByOwner_Id(id);
    }
    public List<Pet> getPetsByVetID(int id) {
        return petRepository.findPetByOwner_Id(id);
    }
    public List<Pet> getPetsByOwner(Owner owner) {
        return petRepository.findByOwner(owner);
    }
    public boolean existsByNameAndUser(String name, Owner owner){return petRepository.existsByNameAndOwner(name,owner);}
    public Pet getPetByNameAndOwner(String name, Owner owner){return petRepository.findPetByNameAndOwner(name, owner);}
    public Optional<Pet> getPetByID(Integer id){
        return petRepository.getPetById(id);
    }
    public ResponseEntity<List<Pet>> getOwnerPets(String authHeader) {
        String username = jwtUtils.getUsernameFromHeader(authHeader);

        Owner owner = ownerService.getOwnerByUsername(username);

        if(owner == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(petRepository.findByOwner(owner),HttpStatus.OK);
    }
}
