package com.example.pupilmed.service;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Pet;
import com.example.pupilmed.models.database.Recommendation;
import com.example.pupilmed.models.database.Visit;
import com.example.pupilmed.repositories.RecommendationRepository;
import com.example.pupilmed.models.server.VetRecommendationRequest;
import com.example.pupilmed.security.auth.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private RecommendationRepository recommendationRepository;
    private VisitService visitService;
    private OwnerService ownerService;
    private PetService petService;
    private JwtUtils jwtUtils;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository, VisitService visitService, OwnerService ownerService, PetService petService, JwtUtils jwtUtils) {
        this.recommendationRepository = recommendationRepository;
        this.visitService = visitService;
        this.ownerService = ownerService;
        this.petService = petService;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<List<Recommendation>> getRecommendationsByPetID(int id) {

        Optional<Pet> pet = petService.getPetByID(id);
        if(pet.isEmpty())return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        List<Visit> visits = visitService.getVisitsByPetID(id);

        if(visits.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

        List<Recommendation> recommendations = new ArrayList<>();
        for (Visit visit : visits) {
            recommendations.add(visit.getRecommendation());
        }

        return new ResponseEntity<>(recommendations,HttpStatus.OK);
    }    public ResponseEntity<List<Recommendation>> getRecommendationsByPetIDAndOwner(int id, String authHeader) {

        String username = jwtUtils.getUsernameFromHeader(authHeader);
        Owner owner = ownerService.getOwnerByUsername(username);
        if(owner==null)return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        Optional<Pet> pet = petService.getPetByID(id);
        if(pet.isEmpty())return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        List<Visit> visits = visitService.getVisitsByPetIDAndOwner(id,owner);

        if(visits.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

        List<Recommendation> recommendations = new ArrayList<>();
        for (Visit visit : visits) {
            recommendations.add(visit.getRecommendation());
        }

        return new ResponseEntity<>(recommendations,HttpStatus.OK);
    }

    public ResponseEntity<String> modifyRecommendation(VetRecommendationRequest payload) {
        if(payload.recommendation().length() < 5)
            return new ResponseEntity<>("Recommendation is too short.", HttpStatus.BAD_REQUEST);

        Optional<Visit> visit = visitService.getVisitByID(payload.visitID());
        if(visit.isPresent() && visit.get().getRecommendation() != null){
            Recommendation recommendation = visit.get().getRecommendation();
            recommendation.setRecommendation(payload.recommendation());
            recommendationRepository.save(recommendation);
            return new ResponseEntity<>("Recommendation edited successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Visit or recommendation does not exist.", HttpStatus.NOT_FOUND);
    }//ToDO rozdzielic sprawdzanie wizyty od rekomendacji

    //todo czy mozna po id rekomendacji czy tylko wizyty

    public ResponseEntity<String> deleteRecommendation(Integer visitID) {
        Optional<Visit> visit = visitService.getVisitByID(visitID);
        if(visit.isPresent()){
            Recommendation recommendation = visit.get().getRecommendation();
            if(recommendation != null){
                visit.get().setRecommendation(null);
                visitService.save(visit.get());
                recommendationRepository.deleteById(recommendation.getId());
                return new ResponseEntity<>("Recommendation deleted successfully.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Recommendation does not exist.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Visit does not exist.", HttpStatus.NOT_FOUND);
    }

public ResponseEntity<String> addRecommendation(VetRecommendationRequest payload) {
    if(payload.recommendation().length() < 5)
        return new ResponseEntity<>("Recommendation is too short.", HttpStatus.BAD_REQUEST);

    if (payload == null || payload.visitID() == null || payload.recommendation() == null) {
        return new ResponseEntity<>("Invalid payload: visitID or recommendation is missing.", HttpStatus.BAD_REQUEST);
    }

    Optional<Visit> optionalVisit = visitService.getVisitByID(payload.visitID());
    if (optionalVisit.isEmpty()) {
        return new ResponseEntity<>("Visit does not exist.", HttpStatus.NOT_FOUND);
    }

    Visit visit = optionalVisit.get();

    if (visit.getRecommendation() != null) {
        return new ResponseEntity<>("Visit already contains a recommendation.", HttpStatus.CONFLICT);
    }

    Recommendation newRecommendation = new Recommendation(payload.recommendation());
    recommendationRepository.save(newRecommendation);
    visit.setRecommendation(newRecommendation);

    try {
        visitService.save(visit);
        return new ResponseEntity<>("Recommendation added successfully.", HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>("Failed to save the recommendation." + e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

}
