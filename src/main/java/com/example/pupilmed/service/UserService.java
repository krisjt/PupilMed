package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Role;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.database.Vet;
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
            response.add(new UserResponse(owner.getUser().getId(),Role.OWNER,owner.getName(),owner.getSurname(),owner.getUser().getUsername(), null,null));
        }

        for(Vet vet : vets){
            response.add(new UserResponse(vet.getUser().getId(),Role.VET,vet.getName(),vet.getSurname(),vet.getUser().getUsername(), vet.getClinicAddress(), vet.getClinicName()));
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

    public ResponseEntity<String> changePassword(String authHeader, String newPassword) {

        if(newPassword == null || newPassword.equals(""))return new ResponseEntity<>("Password can't be empty.", HttpStatus.BAD_REQUEST);

        String username = jwtUtils.getUsernameFromHeader(authHeader);

        Optional<User> optUser = userRepository.findByUsername(username);

        if(optUser.isPresent()){
            User user = optUser.get();

            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);

            return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Couldn't find user.", HttpStatus.NOT_FOUND);
    }

//    public ResponseEntity<String> changePassword(String authHeader, String newPassword) {
//        if(newPassword == null || newPassword.equals("")) {
//            return new ResponseEntity<>("Password can't be empty.", HttpStatus.BAD_REQUEST);
//        }
//
//        String plainPassword;
//        try {
//            if (newPassword.startsWith("{")) {
//                plainPassword = newPassword
//                        .replace("{", "")
//                        .replace("}", "")
//                        .replace("\"password\":", "")
//                        .replace("\"", "")
//                        .trim();
//            } else {
//                plainPassword = newPassword;
//            }
//
//            System.out.println("Extracted plain password: " + plainPassword);
//
//            String username = jwtUtils.getUsernameFromHeader(authHeader);
//            Optional<User> optUser = userRepository.findByUsername(username);
//
//            if(optUser.isPresent()) {
//                User user = optUser.get();
//                String encodedPassword = bCryptPasswordEncoder.encode(plainPassword);
//
//                user.setPassword(encodedPassword);
//                userRepository.save(user);
//
//                User savedUser = userRepository.findByUsername(username).orElseThrow();
//                boolean matches = bCryptPasswordEncoder.matches(plainPassword, savedUser.getPassword());
//                System.out.println("Password verification after save: " + matches);
//
//                return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
//            }
//            return new ResponseEntity<>("Couldn't find user.", HttpStatus.NOT_FOUND);
//
//        } catch (Exception e) {
//            return new ResponseEntity<>("Invalid password format", HttpStatus.BAD_REQUEST);
//        }
//    }
}
