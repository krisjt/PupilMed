package com.example.pupilmed.controllers;

import com.example.pupilmed.models.database.visit.Visit;
import com.example.pupilmed.security.jwt.JwtUtils;
import com.example.pupilmed.service.OwnerService;
import com.example.pupilmed.models.database.pet.Pet;
import com.example.pupilmed.service.PetService;
import com.example.pupilmed.service.RecommendationService;
import com.example.pupilmed.models.database.vet.Vet;
import com.example.pupilmed.service.VetService;
import com.example.pupilmed.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @GetMapping("/visits")
//    public Page<Visit> getVisits(@RequestParam("pageNumber") Integer pageNumber,
//                                 @RequestParam("pageSize") Integer pageSize){
//        if(pageSize != null && pageNumber != null) {
//            Pageable pageable = PageRequest.of(pageNumber, pageSize);
//            return visitService.getAllVisits(pageable);
//        }
//        return null;
//    }

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

