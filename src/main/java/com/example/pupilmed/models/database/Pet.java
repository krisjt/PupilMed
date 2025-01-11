package com.example.pupilmed.models.database;

import jakarta.persistence.*;
import lombok.Builder;
import java.sql.Date;
import java.util.Calendar;

@Entity
@Table(name = "zwierze")
@Builder
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="imie", length = 100)
    private String name;
    @Column(name="gatunek", length = 100)
    private String species;
    @Column(name="rasa")
    private String breed;

    @Column(name="data_urodzenia")
    private Date dateOfBirth;

    @Column(name="uwagi_o_zwierzeciu")
    private String additionalInfo;
    @ManyToOne
    @JoinColumn(name = "wlascicielid", referencedColumnName = "id")
    private Owner owner;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String kind) {
        this.species = kind;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String type) {
        this.breed = type;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Owner getOwnerID() {
        return owner;
    }

    public Integer getAge(){
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(dateOfBirth);

        Calendar now = Calendar.getInstance();

        return now.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
    }

    public void setOwnerID(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind='" + species + '\'' +
                ", type='" + breed + '\'' +
                ", date_of_birth='" + dateOfBirth + '\'' +
                ", additional_info='" + additionalInfo + '\'' +
                ", ownerID='" + owner + '\'' +
                '}';
    }

    public Pet(String name, String species, String breed, Date date_of_birth, String additional_info, Owner owner) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.dateOfBirth = date_of_birth;
        this.additionalInfo = additional_info;
        this.owner = owner;
    }

    public Pet(String name, String species, String breed, Date date_of_birth, Owner ownerID) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.dateOfBirth = date_of_birth;
        this.owner = ownerID;
    }

    public Pet(int id, String name, String species, String breed, Date dateOfBirth, String additionalInfo, Owner owner) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.dateOfBirth = dateOfBirth;
        this.additionalInfo = additionalInfo;
        this.owner = owner;
    }

    public Pet() {
    }
}
//TODO usuwanie kaskadowe jak robic