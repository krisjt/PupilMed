package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.database.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VetRepository extends JpaRepository<Vet,Integer> {

//    @Query("SELECT v FROM Vet v WHERE v.clinicAddress = ?1")
    Optional<Vet> findVetByClinicAddress(String clinicAddress);

    Vet getVetByUser_Username(String username);
    boolean existsByUser(User user);

    Vet getVetByUser(User user);

}
