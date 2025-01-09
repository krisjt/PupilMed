package com.example.pupilmed.database.repositories;

import com.example.pupilmed.database.models.owner.Owner;
import com.example.pupilmed.database.models.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {

    List<Pet> findPetByOwner_Id(Integer id);
    Pet findPetByNameAndOwner(String name, Owner owner);
    boolean existsByNameAndOwner(String name, Owner owner);

}
