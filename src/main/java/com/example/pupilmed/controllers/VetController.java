package com.example.pupilmed.controllers;

import com.example.pupilmed.models.database.Pet;
import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.database.Vet;
import com.example.pupilmed.models.database.Visit;
import com.example.pupilmed.models.database.VisitType;
import com.example.pupilmed.models.server.VetResponse;
import com.example.pupilmed.models.server.VetRecommendationRequest;
import com.example.pupilmed.models.server.VetVisitDetails;
import com.example.pupilmed.models.server.VetVisitRequest;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import com.example.pupilmed.service.*;
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
@RequestMapping("/vet")
@CrossOrigin(origins = "http://localhost:3000")
public class VetController {

    private final VetService vetService;
    private final PetService petService;
    private final OwnerService ownerService;
    private final UserService userService;
    private final SpeciesBreedService speciesBreedService;

    private final VisitTypeService visitTypeService;
    private final RecommendationService recommendationService;
    private final VisitService visitService;
    private final JwtUtils jwtUtils;

    @Autowired
    public VetController(VetService vetService, PetService petService,
                         OwnerService ownerService, UserService userService, SpeciesBreedService speciesBreedService, VisitTypeService visitTypeService, RecommendationService recommendationService,
                         VisitService visitService, JwtUtils jwtUtils) {
        this.vetService = vetService;
        this.userService = userService;
        this.speciesBreedService = speciesBreedService;
        this.visitTypeService = visitTypeService;
        this.visitService = visitService;
        this.petService = petService;
        this.ownerService = ownerService;
        this.recommendationService = recommendationService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/get-pets")
    public List<Pet> getPets(){
        return petService.getPets();
    }

    //TODO: dodac sprawdzanie czy wizyta nalezy do weterynarza, jak nie to nie zwracac
    @GetMapping("/visit-details")
    public VetVisitDetails getVisitDetails(@RequestHeader("Authorization") String authHeader,@RequestParam("visitID") Integer visitID){
        return visitService.getVisitDetails(visitID,authHeader);
    }

    @GetMapping("/visits-by-date")
    public List<Visit> getVisitsByDate(@RequestHeader("Authorization") String authHeader, @RequestParam("startDate") String startDateStr,
                                       @RequestParam("endDate") String endDateStr) throws ParseException {
        String token = authHeader.replace("Bearer ", "");

        if (!jwtUtils.validateJwtToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String username = jwtUtils.getUsernameFromJwtToken(token);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateStr);
        Date endDate = dateFormat.parse(endDateStr);

        return visitService.getVisitsByUsernameBetweenDates(username,startDate,endDate);
    }

    @GetMapping("/account")
    public ResponseEntity<VetResponse> getAccount(@RequestHeader("Authorization") String authHeader){
        String username = getUsernameFromToken(authHeader);

        Vet vet = vetService.getVetByUsername(username);
        return ResponseEntity.ok(new VetResponse(vet.getName(), vet.getSurname(), vet.getClinicName(), vet.getClinicAddress(), username));
    }

    @DeleteMapping("/delete-visit")
    public ResponseEntity<String> deleteVisit(@RequestParam Integer visitID){
        return visitService.deleteVisit(visitID);
    }

    @PutMapping("/modify-visit")
    public ResponseEntity<String> modifyVisit(@RequestBody VetVisitRequest visit) {
        return visitService.modifyVisit(visit);
    }//sprawdzic czy numer telefonu weterynarza istnieje, czy numer wlasiciela istnieje, czy data nie jest zajeta, czy zwierze przypisane do wlasciciela

    @PutMapping("/modify-recommendation")
    public ResponseEntity<String> modifyRecommendation(@RequestBody VetRecommendationRequest recommendation){
        return recommendationService.modifyRecommendation(recommendation);
    }
    @DeleteMapping("/delete-recommendation")
    public ResponseEntity<String> deleteRecommendation(@RequestParam Integer visitID){
        return recommendationService.deleteRecommendation(visitID);
    }

    @PostMapping("/add-recommendation")
    public ResponseEntity<String> addRecommendation(@RequestBody VetRecommendationRequest payload){
        return recommendationService.addRecommendation(payload);
    }

    @PostMapping("/add-visit")
    public ResponseEntity<String> addVisit(@RequestBody VetVisitRequest payload, @RequestHeader("Authorization") String authHeader){

        String username = getUsernameFromToken(authHeader);

        User user = userService.getUserByUsername(username);
        Vet vet = vetService.getVetByUser(user);

        if(vet == null){
            return new ResponseEntity<>("Vet does not exist.", HttpStatus.NOT_FOUND);
        }

        return visitService.addVisit(vet,payload);
    }

    private String getUsernameFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        if (!jwtUtils.validateJwtToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        return jwtUtils.getUsernameFromJwtToken(token);
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

//    @PreAuthorize("hasRole('ADMIN')") do jednego
//TODO zrobic DTO a nie Visit
//TODO ograniczyc mozliwosc wysylania zapytan zwiazanych z nienalezacymi do weterynarza wizytami