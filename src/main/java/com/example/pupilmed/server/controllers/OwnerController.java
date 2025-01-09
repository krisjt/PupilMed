package com.example.pupilmed.server.controllers;

import com.example.pupilmed.database.models.pet.Pet;
import com.example.pupilmed.database.models.visit.Visit;
import com.example.pupilmed.server.service.PetService;
import com.example.pupilmed.database.models.recommendation.Recommendation;
import com.example.pupilmed.server.service.RecommendationService;
import com.example.pupilmed.database.models.owner.Owner;
import com.example.pupilmed.server.service.OwnerService;
import com.example.pupilmed.database.models.user.User;
import com.example.pupilmed.server.service.VetService;
import com.example.pupilmed.server.service.VisitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final VetService vetService;
    private final PetService petService;
    private final OwnerService ownerService;
    private final RecommendationService recommendationService;
    private final VisitService visitService;

    @Autowired
    public OwnerController(VetService vetService, PetService petService,
                           OwnerService ownerService, RecommendationService recommendationService,
                           VisitService visitService) {
        this.vetService = vetService;
        this.visitService = visitService;
        this.petService = petService;
        this.ownerService = ownerService;
        this.recommendationService = recommendationService;
    }

//    @GetMapping("/visits")
//    public List<Visit> getVisits(){
//        return visitService.getVisits();
//    }

//    @GetMapping("/pets")
//    public List<Pet> getPets(@RequestBody int id){
//        return petService.getPets(id);
//    }

    //dane z sesji
    @GetMapping("/account")
    public Owner getAccount(HttpSession session){
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            throw new RuntimeException("No user found in session");
        }
        return ownerService.getOwnerByUserID(user);
    }

    @GetMapping("/recommendations:{petID}")
    public List<Recommendation> getRecommendations(@PathVariable("petID") int id){
        return recommendationService.getRecommendationsByPetID(id);
    }

}
