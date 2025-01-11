package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Pet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet,Integer> {

    List<Pet> findPetByOwner_Id(Integer id);
    List<Pet> findByOwner(Owner owner);
    Pet findPetByNameAndOwner(String name, Owner owner);
    boolean existsByNameAndOwner(String name, Owner owner);
    Optional<Pet> getPetById(Integer id);
//    boolean existsById(Integer id);
}
