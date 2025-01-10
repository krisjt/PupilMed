package com.example.pupilmed.service;

import com.example.pupilmed.models.database.recommendation.Recommendation;
import com.example.pupilmed.models.database.visit.Visit;
import com.example.pupilmed.repositories.RecommendationRepository;
import com.example.pupilmed.models.server.recommendation.VetRecommendationRequest;
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

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository, VisitService visitService) {
        this.recommendationRepository = recommendationRepository;
        this.visitService = visitService;
    }
    public List<Recommendation> getRecommendationsByPetID(int id) {
        List<Visit> visits = visitService.getVisitsByPetID(id);

        List<Recommendation> recommendations = new ArrayList<>();
        for (Visit visit : visits) {
            recommendations.add(visit.getRecommendation());
        }

        return  recommendations;
    }

    public ResponseEntity<String> modifyRecommendation(VetRecommendationRequest payload) {
        System.out.println("payload:" + payload);

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
                recommendationRepository.deleteById((long)recommendation.getId());
                return new ResponseEntity<>("Recommendation deleted successfully.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Recommendation does not exist.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Visit does not exist.", HttpStatus.NOT_FOUND);
    }

public ResponseEntity<String> addRecommendation(VetRecommendationRequest payload) {
    System.out.printf("payload:" + payload);

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
