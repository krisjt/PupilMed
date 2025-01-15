package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Pet;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.server.PetRequest;
import com.example.pupilmed.repositories.PetRepository;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private PetRepository petRepository;
    private OwnerService ownerService;
    private UserService userService;
    private JwtUtils jwtUtils;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PetService(PetRepository petRepository, OwnerService ownerService, UserService userService, JwtUtils jwtUtils) {
        this.petRepository = petRepository;
        this.ownerService = ownerService;
        this.userService = userService;
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

    public ResponseEntity<String> addPet(PetRequest payload) {

        Owner owner = ownerService.getOwnerByUsername(payload.ownerPhoneNumber());
        if(owner == null)
            return new ResponseEntity<>("Właściciel o podanym numerze nie istnieje.", HttpStatus.NOT_FOUND);

        User user = owner.getUser();
        if(user == null)return new ResponseEntity<>("Właściciel o podanym numerze nie istnieje.", HttpStatus.NOT_FOUND);
        if(!user.isActive())return new ResponseEntity<>("Właściciel nie jest aktywny w systemie.", HttpStatus.CONFLICT);
        if(parseDate(payload.petDob()).after(new Date()))return new ResponseEntity<>("Data urodzenia nie może być w przyszłości.",HttpStatus.CONFLICT);
        if(petRepository.existsByNameAndOwner(payload.petName(),owner))return new ResponseEntity<>("Już istnieje zwierzę o podanym imieniu dla tego właściciela.", HttpStatus.CONFLICT);

        if(payload.petDob() == null || payload.petSpecies() == null || payload.petBreed() == null || payload.petName() == null || payload.ownerPhoneNumber() == null)
            return new ResponseEntity<>("Some fields are missing.", HttpStatus.BAD_REQUEST);

        Pet pet = new Pet(payload.petName(), payload.petSpecies(), payload.petBreed(), parseDate(payload.petDob()),owner);

        if(payload.additionalInfo() != null){
            if(payload.additionalInfo().length() < 5)
                return new ResponseEntity<>("Informacje dodatkowe są za krótkie.", HttpStatus.BAD_REQUEST);
            pet.setAdditionalInfo(payload.additionalInfo());
        }
        try {
            petRepository.save(pet);
        }catch(Exception e){
            return new ResponseEntity<>("Adding to db failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Pet added successfully.",HttpStatus.OK);
    }

    public ResponseEntity<String> modifyPet(Integer petID, PetRequest payload) {
        if(petID == null) return new ResponseEntity<>("Pet ID is null - you haven't chosen a pet.", HttpStatus.BAD_REQUEST);

        Optional<Pet> optionalPet = petRepository.getPetById(petID);

        if(optionalPet.isEmpty())return new ResponseEntity<>("Pet does not exist in database.", HttpStatus.NOT_FOUND);

        Pet pet = optionalPet.get();

        Owner owner = ownerService.getOwnerByUsername(payload.ownerPhoneNumber());
        if(owner == null)return new ResponseEntity<>("Właściciel o podanym numerze nie istnieje.", HttpStatus.NOT_FOUND);
        if(!owner.getUser().isActive())return new ResponseEntity<>("Właściciel nie jest aktywny w systemie.", HttpStatus.CONFLICT);
        if(parseDate(payload.petDob()).after(new Date()))return new ResponseEntity<>("Data urodzenia nie może być w przyszłości.",HttpStatus.CONFLICT);
        if(petRepository.existsByNameAndOwner(payload.petName(),owner))return new ResponseEntity<>("Już istnieje zwierzę o podanym imieniu dla tego właściciela.", HttpStatus.CONFLICT);

        if(payload.petSpecies() != null && !payload.petSpecies().equals(""))pet.setSpecies(payload.petSpecies());
        if(payload.petDob() != null && !payload.petDob().equals(""))pet.setDateOfBirth(parseDate(payload.petDob()));
        if(payload.petBreed() != null && !payload.petBreed().equals(""))pet.setBreed(payload.petBreed());
        if(payload.additionalInfo() != null){
            if(payload.additionalInfo().length() < 5)return new ResponseEntity<>("Informacje dodatkowe są za krótkie.", HttpStatus.BAD_REQUEST);
            pet.setAdditionalInfo(payload.additionalInfo());
        }
        if(payload.petName() != null && !payload.petName().equals(""))pet.setName(payload.petName());

        pet.setOwnerID(owner);

        petRepository.save(pet);

        return new ResponseEntity<>("Pet modified successfully.",HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deletePetById(Integer petID) {
        Pet pet = entityManager.find(Pet.class, petID);
        if (pet != null) {
            entityManager.remove(pet);
            return new ResponseEntity<>("Pet has been successfully deleted.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Pet does not exist in database.", HttpStatus.NOT_FOUND);
    }

    private java.sql.Date parseDate(String date) {
        return java.sql.Date.valueOf(date);
    }

    private Time parseTime(String hour){
        try {
            if (hour.matches("^\\d{2}:\\d{2}$")) {
                hour = hour + ":00";
            }
            return Time.valueOf(hour);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid time format. Expected HH:mm or HH:mm:ss.");
        }
    }
}
