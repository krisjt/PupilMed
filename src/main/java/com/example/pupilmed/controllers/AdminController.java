package com.example.pupilmed.controllers;

import com.example.pupilmed.models.database.visit.Visit;
import com.example.pupilmed.models.server.recommendation.VetRecommendationRequest;
import com.example.pupilmed.models.server.visit.VetVisitDetails;
import com.example.pupilmed.models.server.visit.VetVisitRequest;
import com.example.pupilmed.security.jwt.JwtUtils;
import com.example.pupilmed.service.OwnerService;
import com.example.pupilmed.models.database.pet.Pet;
import com.example.pupilmed.service.PetService;
import com.example.pupilmed.service.RecommendationService;
import com.example.pupilmed.models.database.vet.Vet;
import com.example.pupilmed.service.VetService;
import com.example.pupilmed.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final VetService vetService;
    private final PetService petService;
    private final OwnerService ownerService;
    private final RecommendationService recommendationService;
    private final VisitService visitService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AdminController(VetService vetService, PetService petService,
                           OwnerService ownerService, RecommendationService recommendationService,
                           VisitService visitService, JwtUtils jwtUtils) {
        this.vetService = vetService;
        this.visitService = visitService;
        this.petService = petService;
        this.ownerService = ownerService;
        this.recommendationService = recommendationService;
        this.jwtUtils = jwtUtils;
    }

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
    public VetVisitDetails getVisitDetails(@RequestParam("visitID") Integer visitID) {
        return visitService.getVisitDetails(visitID);
    }

    @PutMapping("/modify-visit")
    public ResponseEntity<String> modifyVisit(@RequestBody VetVisitRequest payload) {
        System.out.println("payload:" + payload);
        if(payload.vetPhoneNumber() == null)
            return new ResponseEntity<>("Vet phone number is empty.", HttpStatus.BAD_REQUEST);
        return visitService.modifyVisit(payload);
    }

    @PostMapping("/add-recommendation")
    public ResponseEntity<String> addRecommendation(@RequestBody VetRecommendationRequest payload){
        System.out.println("AhAHAHHA");
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

        String username = jwtUtils.getUsernameFromJwtToken(token);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateStr);
        Date endDate = dateFormat.parse(endDateStr);

        return visitService.getVisitSBetweenDates(startDate,endDate);
    }

    //NIE SPRAWDZONE STARE EP
    @PostMapping("/add-vet")
    public void addVet(@RequestBody Vet vet){
        vetService.addVet(vet);
    }

    @DeleteMapping(path = "/delete-vet:{vetId}")
    public void deleteVet(@PathVariable("vetId") int id){
        vetService.deleteVet(id);
    }
    @GetMapping("/get-pets")
    public List<Pet> getPets(){
        return petService.getPets();
    }
    @PutMapping(path = "/update-vet:{vetId}")
    public void updateVet(@RequestBody Vet vet, @PathVariable("vetId") int id){
        vetService.updateVet(id, vet);
    }
}

