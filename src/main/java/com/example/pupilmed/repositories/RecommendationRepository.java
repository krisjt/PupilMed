package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation,Integer> {

//    Recommendation getRecommendationById(Integer id);

//    Recommendation getById(Long id);

}
