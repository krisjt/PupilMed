package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.Vet;
import com.example.pupilmed.models.database.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Integer> {
    public List<Visit> getVisitsByPet_IdAndPet_Owner(int id, Owner owner);
    public List<Visit> getVisitsByVet_Id(int id);
    List<Visit> getVisitByDateBetween(Date start, Date end);
    Optional<Visit> getVisitById(int id);
    List<Visit> getVisitsByPet_Id(Integer petID);
    List<Visit> getVisitsByPet_Owner(Owner owner);
//    OVisit> getVisitById(int id);
    boolean existsByDateAndVetAndHour(Date date, Vet vet, Time hour);
    public List<Visit> getVisitsByVet_IdAndDateBetween(int id, Date start, Date end);
    List<Visit> getVisitsByPet_IdAndDateBetween(int id, Date startDate, Date endDate);
    List<Visit> getVisitsByVetAndDateAndHour(Vet vet, Date date, Time hour);
    Optional<Visit> getVisitByIdAndPet_Owner(int id, Owner owner);
    Optional<Visit> getVisitsByIdAndVet(int id, Vet vet);
//    public Page<Visit> getAllVisits(Pageable pageable);
}
