package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Role;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.database.Vet;
import com.example.pupilmed.models.server.UserRequest;
import com.example.pupilmed.repositories.UserRepository;
import com.example.pupilmed.repositories.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VetService {

    private final VetRepository vetRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public VetService(VetRepository vetRepository, UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.vetRepository = vetRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<Vet> getVets(){
        return vetRepository.findAll();
    }
    public Vet getVetByUser(User user){
        return vetRepository.getVetByUser(user);
    }
    public Vet getVetByUsername(String user){
        return vetRepository.getVetByUser_Username(user);
    }

    public void deleteVet(int id) {
        boolean exists = vetRepository.existsById(id);
        if(exists){
            vetRepository.deleteById(id);
        }
        else{
            throw new IllegalStateException("Vet with id = " + id + " does not exist.");
        }
    }

    public void updateVet(int id, Vet vet) {
        if(id == vet.getId()){
            Optional<Vet> oldVet = vetRepository.findById(id);
            if(oldVet.isPresent() && oldVet.get().getUser()==vet.getUser()) {
                boolean exists = vetRepository.existsById(id);
                if (exists) {
                    vetRepository.save(vet);
                } else {
                    throw new IllegalStateException("Vet with id = " + id + " does not exist.");
                }
            }
            else  throw new IllegalStateException("Cannot change user id! Trying to change from " + oldVet.get().getUser() + " to " + vet.getUser() + ".");
        }
        else throw new IllegalStateException("Cannot change id of vet from " + id + " to " + vet.getId() + ".");
    }

    public ResponseEntity<String> addVet(UserRequest payload) {
        if(payload.phoneNumber() == null || payload.role() == null || payload.name() == null || payload.surname() == null || payload.clinicName() == null || payload.clinicAddress() == null || payload.password() == null)
            return new ResponseEntity<>("Fields are missing.", HttpStatus.BAD_REQUEST);

        if(userRepository.existsByUsername(payload.phoneNumber()))return new ResponseEntity<>("Username is taken.", HttpStatus.CONFLICT);

        if(payload.password().length() < 5)return new ResponseEntity<>("Password is too short.", HttpStatus.BAD_REQUEST);

        User user = new User(payload.phoneNumber(), bCryptPasswordEncoder.encode(payload.password()), true, Role.VET);
        userRepository.save(user);

        if(!userRepository.existsByUsername(payload.phoneNumber()))return new ResponseEntity<>("Error adding user.",HttpStatus.INTERNAL_SERVER_ERROR);

        Vet vet = new Vet(payload.name(), payload.surname(), payload.clinicName(), payload.clinicAddress(), user);
        vetRepository.save(vet);

        return new ResponseEntity<>("Vet added successfully.",HttpStatus.OK);
    }

    public ResponseEntity<String> modifyVet(UserRequest payload, Integer id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Vet vet = vetRepository.getVetByUser(user);

            if(payload.password() == null || payload.password().length() < 5)return new ResponseEntity<>("Password is too short.", HttpStatus.BAD_REQUEST);
            if(payload.phoneNumber() == null)return new ResponseEntity<>("Phone number is empty.", HttpStatus.BAD_REQUEST);
            if(userRepository.existsByUsername(payload.phoneNumber()))return new ResponseEntity<>("Username is taken.",HttpStatus.CONFLICT);
            user.setPassword(bCryptPasswordEncoder.encode(payload.password()));
            user.setUsername(payload.phoneNumber());

            userRepository.save(user);

            if(payload.name() != null)vet.setName(payload.name());
            if(payload.surname() != null)vet.setSurname(payload.surname());
            if(payload.clinicName()!=null)vet.setClinicName(payload.clinicName());
            if(payload.clinicAddress()!=null)vet.setClinicAddress(payload.clinicAddress());

            vet.setUserId(user);
            vetRepository.save(vet);

            return new ResponseEntity<>("User modified successfully.",HttpStatus.OK);
        }
        return new ResponseEntity<>("User does not exist.",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteUser(Optional<User> user) {
        Vet vet = vetRepository.getVetByUser(user.get());

        if(vet == null)return new ResponseEntity<>("Vet does not exist in database.", HttpStatus.NOT_FOUND);

        vetRepository.delete(vet);
        userRepository.delete(user.get());

        return new ResponseEntity<>("Vet deleted successfully.", HttpStatus.OK);
    }
}
