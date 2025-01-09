package com.example.pupilmed.server.service;

import com.example.pupilmed.database.models.owner.Owner;
import com.example.pupilmed.database.models.pet.Pet;
import com.example.pupilmed.database.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
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
    public boolean existsByNameAndUser(String name, Owner owner){return petRepository.existsByNameAndOwner(name,owner);}
    public Pet getPetByNameAndOwner(String name, Owner owner){return petRepository.findPetByNameAndOwner(name, owner);}
}
