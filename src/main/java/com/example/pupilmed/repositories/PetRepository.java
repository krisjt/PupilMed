package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.owner.Owner;
import com.example.pupilmed.models.database.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {

    List<Pet> findPetByOwner_Id(Integer id);
    List<Pet> findByOwner(Owner owner);
    Pet findPetByNameAndOwner(String name, Owner owner);
    boolean existsByNameAndOwner(String name, Owner owner);

}
