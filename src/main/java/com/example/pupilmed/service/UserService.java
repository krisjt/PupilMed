package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Role;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.database.Vet;
import com.example.pupilmed.models.server.IsActiveRequest;
import com.example.pupilmed.models.server.PasswordChangeRequest;
import com.example.pupilmed.models.server.UserRequest;
import com.example.pupilmed.models.server.UserResponse;
import com.example.pupilmed.repositories.UserRepository;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final JwtUtils jwtUtils;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, VetService vetService, OwnerService ownerService, JwtUtils jwtUtils, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.vetService = vetService;
        this.ownerService = ownerService;
        this.jwtUtils = jwtUtils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
            response.add(new UserResponse(owner.getUser().getId(),Role.OWNER,owner.getName(),owner.getSurname(),owner.getUser().getUsername(), null,null,owner.getUser().isActive()));
        }

        for(Vet vet : vets){
            response.add(new UserResponse(vet.getUser().getId(),Role.VET,vet.getName(),vet.getSurname(),vet.getUser().getUsername(), vet.getClinicAddress(), vet.getClinicName(),vet.getUser().isActive()));
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

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())return new ResponseEntity<>("User with id does not exist.", HttpStatus.NOT_FOUND);

        if(!userRepository.existsByRoleAndId(Role.valueOf(payload.role()), id))
            return new ResponseEntity<>("Role is not valid.", HttpStatus.BAD_REQUEST);

        if(user.get().isActive())return new ResponseEntity<>("You can't modify not active user.",HttpStatus.CONFLICT);

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

    public ResponseEntity<String> changePassword(String authHeader, PasswordChangeRequest payload) {

        if(payload.newPassword() == null || payload.newPassword().equals(""))return new ResponseEntity<>("New password can't be empty.", HttpStatus.BAD_REQUEST);
        if(payload.newPassword().length() < 5)return new ResponseEntity<>("Password is too short.", HttpStatus.BAD_REQUEST);
        if(payload.currentPassword() == null || payload.currentPassword().equals(""))return new ResponseEntity<>("Old password can't be empty.", HttpStatus.BAD_REQUEST);
        if(payload.currentPassword().equals(payload.newPassword()))return new ResponseEntity<>("Passwords don't differ.", HttpStatus.CONFLICT);
        String username = jwtUtils.getUsernameFromHeader(authHeader);

        Optional<User> optUser = userRepository.findByUsername(username);

        if(optUser.isPresent()){
            User user = optUser.get();

            if (!bCryptPasswordEncoder.matches(payload.currentPassword(), user.getPassword()))
                return new ResponseEntity<>("Passwords don't match.", HttpStatus.CONFLICT);

            user.setPassword(bCryptPasswordEncoder.encode(payload.newPassword()));
            userRepository.save(user);

            return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Couldn't find user.", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> changeIsActive(IsActiveRequest payload) {
        Optional<User> user = userRepository.findById(payload.id());
        if(user.isEmpty())return new ResponseEntity<>("User doesn't exist.", HttpStatus.NOT_FOUND);

        user.get().setActive(payload.isActive());

        if(payload.isActive())
            return new ResponseEntity<>("User is set to active.",HttpStatus.OK);
        else
            return new ResponseEntity<>("User is set to not active.",HttpStatus.OK);
    }
}
//Pagination
//{
//content: []
//pages : int
//records: int
//}
