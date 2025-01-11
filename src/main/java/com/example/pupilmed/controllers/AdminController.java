package com.example.pupilmed.controllers;

import com.example.pupilmed.models.database.Visit;
import com.example.pupilmed.models.database.VisitType;
import com.example.pupilmed.models.server.*;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import com.example.pupilmed.service.*;
import com.example.pupilmed.models.database.Pet;
import com.example.pupilmed.models.database.Vet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final VetService vetService;
    private final PetService petService;
    private final SpeciesBreedService speciesBreedService;
    private final UserService userService;
    private final VisitTypeService visitTypeService;
    private final OwnerService ownerService;
    private final RecommendationService recommendationService;
    private final VisitService visitService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AdminController(VetService vetService, PetService petService,
                           SpeciesBreedService speciesBreedService, UserService userService, VisitTypeService visitTypeService, OwnerService ownerService, RecommendationService recommendationService,
                           VisitService visitService, JwtUtils jwtUtils) {
        this.vetService = vetService;
        this.speciesBreedService = speciesBreedService;
        this.userService = userService;
        this.visitTypeService = visitTypeService;
        this.visitService = visitService;
        this.petService = petService;
        this.ownerService = ownerService;
        this.recommendationService = recommendationService;
        this.jwtUtils = jwtUtils;
    }

    // VISITS
    @PostMapping("/add-visit")
    public ResponseEntity<String> addVisit(@RequestBody VetVisitRequest payload ){

        if(payload.vetPhoneNumber() != null) {
            String vetPhoneNumber = payload.vetPhoneNumber();

            Vet vet = vetService.getVetByUsername(vetPhoneNumber);

            if (vet != null)
                return visitService.addVisit(vet, payload);

            return new ResponseEntity<>("Vet doesn't exist.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Vet phone number is empty.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/visit-details")
    public VetVisitDetails getVisitDetails(@RequestHeader("Authorization") String authHeader, @RequestParam("visitID") Integer visitID) {
        return visitService.getVisitDetails(visitID,authHeader);
    }

    @PutMapping("/modify-visit")
    public ResponseEntity<String> modifyVisit(@RequestBody VetVisitRequest payload) {
        if(payload.vetPhoneNumber() == null)
            return new ResponseEntity<>("Vet phone number is empty.", HttpStatus.BAD_REQUEST);
        return visitService.modifyVisit(payload);
    }

    @DeleteMapping("/delete-visit")
    public ResponseEntity<String> deleteVisit(@RequestParam Integer visitID){
        return visitService.deleteVisit(visitID);
    }
    @GetMapping("/visits-all")
    public List<Visit> getAllVisits(){
        return visitService.getAllVisits();
    }

    @GetMapping("/visits-by-date")
    public List<Visit> getVisitsByDate(@RequestHeader("Authorization") String authHeader, @RequestParam("startDate") String startDateStr,
                                       @RequestParam("endDate") String endDateStr) throws ParseException {
        String token = authHeader.replace("Bearer ", "");

        if (!jwtUtils.validateJwtToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateStr);
        Date endDate = dateFormat.parse(endDateStr);

        return visitService.getVisitSBetweenDates(startDate,endDate);
    }

    // RECOMMENDATIONS
    @PostMapping("/add-recommendation")
    public ResponseEntity<String> addRecommendation(@RequestBody VetRecommendationRequest payload){
        return recommendationService.addRecommendation(payload);
    }

    @DeleteMapping("/delete-recommendation")
    public ResponseEntity<String> deleteRecommendation(@RequestParam Integer visitID){
        return recommendationService.deleteRecommendation(visitID);
    }

    @PutMapping("/modify-recommendation")
    public ResponseEntity<String> modifyRecommendation(@RequestBody VetRecommendationRequest recommendation){
        return recommendationService.modifyRecommendation(recommendation);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(@RequestParam("petID") Integer petID){
        return recommendationService.getRecommendationsByPetID(petID);
    }


    //PETS
    @GetMapping("/get-pets")
    public List<Pet> getPets(){
        return petService.getPets();
    }

    @PostMapping("/add-pet")
    public ResponseEntity<String> addPet(@RequestBody PetRequest payload){
        return petService.addPet(payload);
    }

    @PutMapping("/modify-pet")
    public ResponseEntity<String> modifyPet(@RequestBody PetRequest payload, @RequestParam("petID") Integer petID){
        return petService.modifyPet(petID,payload);
    }

    @DeleteMapping("/delete-pet")
    public ResponseEntity<String> deletePet(@RequestParam("petID")Integer petID){
        return petService.deletePetById(petID);
    }


    @GetMapping("/get-users")
    public List<UserResponse> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody UserRequest payload){
        return userService.addUser(payload);
    }

    @PutMapping("/modify-user")
    public ResponseEntity<String> modifyUser(@RequestBody UserRequest payload, @RequestParam("userID") Integer userID){
        return userService.modifyUser(payload, userID);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam("userID") Integer userID){
        return userService.deleteUser(userID);
    }

    //POMOCNICZE
    @GetMapping(path = "/get-visit-types")
    public List<VisitType> getVisitTypes(){
        return visitTypeService.getAll();
    }

    @GetMapping(path = "/get-species-breed")
    public Map<String,List<String>> getSpeciesBreed(){
        return speciesBreedService.getAll();
    }
}

