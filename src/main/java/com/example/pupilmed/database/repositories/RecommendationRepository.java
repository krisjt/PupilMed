package com.example.pupilmed.database.repositories;

import com.example.pupilmed.database.models.recommendation.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation,Long> {

//    Recommendation getRecommendationById(Integer id);

//    Recommendation getById(Long id);
}
