package com.example.pupilmed.controllers;

import com.example.pupilmed.models.database.Pet;
import com.example.pupilmed.models.database.Visit;
import com.example.pupilmed.models.database.VisitType;
import com.example.pupilmed.models.server.OwnerResponse;
import com.example.pupilmed.models.server.PasswordChangeRequest;
import com.example.pupilmed.models.server.RecommendationResponse;
import com.example.pupilmed.models.server.VetVisitDetails;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import com.example.pupilmed.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final VetService vetService;
    private final PetService petService;
    private final OwnerService ownerService;
    private final RecommendationService recommendationService;
    private final VisitTypeService visitTypeService;
    private final UserService userService;
    private final SpeciesBreedService speciesBreedService;
    private final VisitService visitService;
    private final JwtUtils jwtUtils;

    @Autowired
    public OwnerController(VetService vetService, PetService petService,
                           OwnerService ownerService, RecommendationService recommendationService,
                           VisitTypeService visitTypeService, UserService userService, SpeciesBreedService speciesBreedService, VisitService visitService, JwtUtils jwtUtils) {
        this.vetService = vetService;
        this.visitTypeService = visitTypeService;
        this.userService = userService;
        this.speciesBreedService = speciesBreedService;
        this.visitService = visitService;
        this.petService = petService;
        this.ownerService = ownerService;
        this.recommendationService = recommendationService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/visits-by-date")
    public List<Visit> getVisitsByDate(@RequestHeader("Authorization") String authHeader, @RequestParam("startDate") String startDate,
                                       @RequestParam("endDate") String endDate) throws ParseException {

        return visitService.getVisitsByOwnerUsernameBetweenDates(authHeader,startDate,endDate);
    }

    @GetMapping("/visit-details")
    public VetVisitDetails getVisitDetails(@RequestHeader("Authorization") String authHeader, @RequestParam("visitID") Integer visitID) {
        return visitService.getVisitDetails(visitID,authHeader);
    }

    @GetMapping("/account")
    public ResponseEntity<OwnerResponse> getAccount(@RequestHeader("Authorization") String authHeader){
        return ownerService.getAccount(authHeader);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(@RequestHeader("Authorization") String authHeader, @RequestParam("petID") int id){
        return recommendationService.getRecommendationsByPetIDAndOwner(id,authHeader);
    }

    @GetMapping("/visits")
    public List<Visit> getVisits(@RequestHeader("Authorization") String authHeader){
        return visitService.getOwnerVisitsByUsername(authHeader);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getPets(@RequestHeader("Authorization") String authHeader){
        return petService.getOwnerPets(authHeader);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String authHeader, @RequestBody PasswordChangeRequest payload){
        return userService.changePassword(authHeader,payload);
    }

    @GetMapping(path = "/get-visit-types")
    public List<VisitType> getVisitTypes(){
        return visitTypeService.getAll();
    }

    @GetMapping(path = "/get-species-breed")
    public Map<String,List<String>> getSpeciesBreed(){
        return speciesBreedService.getAll();
    }
}
