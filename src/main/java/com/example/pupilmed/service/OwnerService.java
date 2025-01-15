package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Role;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.server.OwnerResponse;
import com.example.pupilmed.models.server.UserRequest;
import com.example.pupilmed.repositories.OwnerRepository;
import com.example.pupilmed.repositories.UserRepository;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private JwtUtils jwtUtils;

    @Autowired
    public OwnerService(OwnerRepository userRepository, UserRepository userRepository1, PasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils) {
        this.ownerRepository = userRepository;
        this.userRepository = userRepository1;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
    }
    public List<Owner> findAll(){return ownerRepository.findAll();}
    public Owner getOwnerByUserID(User user) {
        return ownerRepository.findOwnerByUser(user);
    }
    public Owner getOwnerByUsername(String user) {
        return ownerRepository.findOwnerByUser_Username(user);
    }
    public Owner getOwnerByUser(User user){
        return ownerRepository.findOwnerByUser(user);
    }

    public ResponseEntity<OwnerResponse> getAccount(String authHeader){
        if(authHeader == null){
            return new ResponseEntity<>(new OwnerResponse(null,null, null), HttpStatus.FORBIDDEN);
        }
        String username = jwtUtils.getUsernameFromHeader(authHeader);

        Owner owner = getOwnerByUsername(username);

        if(owner == null){
            return new ResponseEntity<>(new OwnerResponse(null,null, null), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new OwnerResponse(owner.getName(),owner.getSurname(),username), HttpStatus.OK);
    }

    public ResponseEntity<String> addOwner(UserRequest payload) {
        if(payload.phoneNumber() == null || payload.role() == null || payload.name() == null || payload.surname() == null || payload.password() == null)
            return new ResponseEntity<>("Fields are missing.", HttpStatus.BAD_REQUEST);

        if(userRepository.existsByUsername(payload.phoneNumber()))return new ResponseEntity<>("Już istnieje użytkownik o tym numerze.", HttpStatus.CONFLICT);

        if(payload.password().length() < 5)return new ResponseEntity<>("Hasło jest za krótkie.", HttpStatus.BAD_REQUEST);

        User user = new User(payload.phoneNumber(), bCryptPasswordEncoder.encode(payload.password()), true, Role.OWNER);
        userRepository.save(user);

        Owner owner = new Owner(payload.name(), payload.surname(), user);
        ownerRepository.save(owner);

        return new ResponseEntity<>("Owner added successfully.",HttpStatus.OK);
    }

    public ResponseEntity<String> modifyOwner(UserRequest payload, Integer id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){

            User user = optionalUser.get();
            Owner owner = ownerRepository.findOwnerByUser(user);

            if(payload.phoneNumber() == null)return new ResponseEntity<>("Phone number is empty.", HttpStatus.BAD_REQUEST);
            if (!user.getUsername().equals(payload.phoneNumber()) && userRepository.existsByUsername(payload.phoneNumber())) {
                return new ResponseEntity<>("Już istnieje użytkownik o tym numerze.", HttpStatus.CONFLICT);
            }

            user.setUsername(payload.phoneNumber());

            userRepository.save(user);

            if(payload.name() != null)owner.setName(payload.name());
            if(payload.surname() != null)owner.setSurname(payload.surname());

            owner.setUserID(user);
            ownerRepository.save(owner);
            return new ResponseEntity<>("User modified successfully.",HttpStatus.OK);
        }
        return new ResponseEntity<>("User does not exist.",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteUser(Optional<User> user) {
        Owner owner = ownerRepository.findOwnerByUser(user.get());

        if(owner == null)return new ResponseEntity<>("Owner does not exist in database.", HttpStatus.NOT_FOUND);

        ownerRepository.delete(owner);
        userRepository.delete(user.get());

        return new ResponseEntity<>("Owner deleted successfully.", HttpStatus.OK);
    }
}
