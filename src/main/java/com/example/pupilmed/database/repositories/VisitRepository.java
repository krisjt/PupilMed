package com.example.pupilmed.database.repositories;

import com.example.pupilmed.database.models.pet.Pet;
import com.example.pupilmed.database.models.vet.Vet;
import com.example.pupilmed.database.models.visit.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Long> {
    public List<Visit> getVisitsByPet_Id(int id);
    public List<Visit> getVisitsByVet_Id(int id);
    Optional<Visit> getVisitById(int id);
    boolean existsByDateAndVetAndHour(Date date, Vet vet, Time hour);
    public List<Visit> getVisitsByVet_IdAndDateBetween(int id, Date start, Date end);
//    public Page<Visit> getAllVisits(Pageable pageable);
}
