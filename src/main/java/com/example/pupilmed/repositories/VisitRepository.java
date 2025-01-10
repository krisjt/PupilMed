package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.vet.Vet;
import com.example.pupilmed.models.database.visit.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Long> {
    public List<Visit> getVisitsByPet_Id(int id);
    public List<Visit> getVisitsByVet_Id(int id);
    List<Visit> getVisitByDateBetween(Date start, Date end);
    Optional<Visit> getVisitById(int id);
//    OVisit> getVisitById(int id);
    boolean existsByDateAndVetAndHour(Date date, Vet vet, Time hour);
    public List<Visit> getVisitsByVet_IdAndDateBetween(int id, Date start, Date end);
    List<Visit> getVisitsByVetAndDateAndHour(Vet vet, Date date, Time hour);
//    public Page<Visit> getAllVisits(Pageable pageable);
}
