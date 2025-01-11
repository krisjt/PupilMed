package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Role;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.database.Vet;
import com.example.pupilmed.models.server.UserRequest;
import com.example.pupilmed.models.server.UserResponse;
import com.example.pupilmed.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private VetService vetService;
    private OwnerService ownerService;

    @Autowired
    public UserService(UserRepository userRepository, VetService vetService, OwnerService ownerService) {
        this.userRepository = userRepository;
        this.vetService = vetService;
        this.ownerService = ownerService;
    }

    public User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public boolean existsByUsernameAndRole(String phoneNumber, Role role) {
        return userRepository.existsByUsernameAndRole(phoneNumber,role);
    }

    public List<UserResponse> getAllUsers(){
        List<Owner> owners = ownerService.findAll();
        List<Vet> vets = vetService.getVets();
        List<UserResponse> response = new ArrayList<>();

        for(Owner owner : owners){
            response.add(new UserResponse(owner.getId(),Role.OWNER,owner.getName(),owner.getSurname(),owner.getUser().getUsername()));
        }

        for(Vet vet : vets){
            response.add(new UserResponse(vet.getId(),Role.VET,vet.getName(),vet.getSurname(),vet.getUser().getUsername()));
        }

        return response;
    }

    public ResponseEntity<String> addUser(UserRequest payload) {
        if(Objects.equals(payload.role(), Role.VET.toString()))
            return vetService.addVet(payload);

        if(Objects.equals(payload.role(), Role.OWNER.toString()))
            return ownerService.addOwner(payload);

        return new ResponseEntity<>("Role unknown.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> modifyUser(UserRequest payload, Integer id) {

        if(!userRepository.existsByRoleAndId(Role.valueOf(payload.role()), id))
            return new ResponseEntity<>("Role is not valid.", HttpStatus.BAD_REQUEST);

        if(Objects.equals(payload.role(), Role.VET.toString()))
            return vetService.modifyVet(payload, id);

        if(Objects.equals(payload.role(), Role.OWNER.toString()))
            return ownerService.modifyOwner(payload, id);

        return new ResponseEntity<>("Role unknown.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteUser(Integer id) {

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())return new ResponseEntity<>("User doesn't exist.", HttpStatus.NOT_FOUND);

        if(user.get().getRole() == Role.OWNER)return ownerService.deleteUser(user);
        if(user.get().getRole() == Role.VET)return vetService.deleteUser(user);

        return new ResponseEntity<>("Unknown role.", HttpStatus.NOT_FOUND);
    }
}
