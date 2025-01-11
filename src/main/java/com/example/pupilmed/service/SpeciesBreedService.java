package com.example.pupilmed.service;

import com.example.pupilmed.models.database.SpeciesBreed;
import com.example.pupilmed.repositories.SpeciesBreedRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpeciesBreedService {

    SpeciesBreedRepository speciesBreedRepository;

    public SpeciesBreedService(SpeciesBreedRepository speciesBreedRepository) {
        this.speciesBreedRepository = speciesBreedRepository;
    }

    public Map<String, List<String>> getAll() {
        List<SpeciesBreed> speciesBreeds = speciesBreedRepository.findAll();

        Map<String, List<String>> response = new HashMap<>();

        for (SpeciesBreed speciesBreed : speciesBreeds) {
            String species = speciesBreed.getSpecies();
            String breed = speciesBreed.getBreed();

            response.putIfAbsent(species, new ArrayList<>());

            response.get(species).add(breed);
        }
        return response;
    }

}
