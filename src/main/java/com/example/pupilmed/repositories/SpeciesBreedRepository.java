package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.SpeciesBreed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesBreedRepository extends JpaRepository<SpeciesBreed,String> {
}
