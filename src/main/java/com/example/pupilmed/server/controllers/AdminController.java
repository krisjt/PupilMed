package com.example.pupilmed.server.controllers;

import com.example.pupilmed.database.models.visit.Visit;
import com.example.pupilmed.server.security.jwt.JwtUtils;
import com.example.pupilmed.server.service.OwnerService;
import com.example.pupilmed.database.models.pet.Pet;
import com.example.pupilmed.server.service.PetService;
import com.example.pupilmed.server.service.RecommendationService;
import com.example.pupilmed.database.models.vet.Vet;
import com.example.pupilmed.server.service.VetService;
import com.example.pupilmed.server.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

