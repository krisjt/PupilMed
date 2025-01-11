package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitTypeRepository extends JpaRepository<VisitType,String> {
}
