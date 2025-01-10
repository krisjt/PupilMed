package com.example.pupilmed.controllers;

import com.example.pupilmed.models.database.vet.Vet;
import com.example.pupilmed.models.database.visit.Visit;
import com.example.pupilmed.models.server.OwnerResponse;
import com.example.pupilmed.models.server.visit.VetVisitDetails;
import com.example.pupilmed.security.jwt.JwtUtils;
import com.example.pupilmed.service.PetService;
import com.example.pupilmed.models.database.recommendation.Recommendation;
import com.example.pupilmed.service.RecommendationService;
import com.example.pupilmed.models.database.owner.Owner;
import com.example.pupilmed.service.OwnerService;
import com.example.pupilmed.models.database.user.User;
import com.example.pupilmed.service.VetService;
import com.example.pupilmed.service.VisitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final VetService vetService;
    private final PetService petService;
    private final OwnerService ownerService;
    private final RecommendationService recommendationService;
    private final VisitService visitService;
    private final JwtUtils jwtUtils;

    @Autowired
    public OwnerController(VetService vetService, PetService petService,
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
//    public List<Visit> getVisits(){
//        return visitService.getVisits();
//    }

//    @GetMapping("/pets")
//    public List<Pet> getPets(@RequestBody int id){
//        return petService.getPets(id);
//    }

    //dane z sesji
//    @GetMapping("/account")
//    public Owner getAccount(HttpSession session){
//        User user = (User) session.getAttribute("loggedInUser");
//
//        if (user == null) {
//            throw new RuntimeException("No user found in session");
//        }
//        return ownerService.getOwnerByUserID(user);
//    }

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

        return visitService.getVisitsByOwnerUsernameBetweenDates(username,startDate,endDate);
    }

    @GetMapping("/visit-details")
    public VetVisitDetails getVisitDetails(@RequestParam("visitID") Integer visitID) {
        return visitService.getVisitDetails(visitID);
    }

    @GetMapping("/account")
    public ResponseEntity<OwnerResponse> getAccount(@RequestHeader("Authorization") String authHeader){
        String username = getUsernameFromToken(authHeader);

        Owner owner = ownerService.getOwnerByUsername(username);
        return ResponseEntity.ok(new OwnerResponse(owner.getName(), owner.getSurname(), username));
    }

    @GetMapping("/recommendations:{petID}")
    public List<Recommendation> getRecommendations(@PathVariable("petID") int id){
        return recommendationService.getRecommendationsByPetID(id);
    }

    private String getUsernameFromToken(String authHeader){
        String token = authHeader.replace("Bearer ", "");

        if (!jwtUtils.validateJwtToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        return jwtUtils.getUsernameFromJwtToken(token);
    }

}
