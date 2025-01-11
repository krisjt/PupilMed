package com.example.pupilmed.models.database.speciesBreed;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "gatunek_rasa")
@Builder
public class SpeciesBreed {

    @Id
    @Column(name="rasa")
    String breed;
    @Column(name="gatunek")
    String species;

    @Override
    public String toString() {
        return "SpeciesBreed{" +
                "breed='" + breed + '\'' +
                ", species='" + species + '\'' +
                '}';
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public SpeciesBreed() {
    }

    public SpeciesBreed(String breed, String species) {
        this.breed = breed;
        this.species = species;
    }
}
