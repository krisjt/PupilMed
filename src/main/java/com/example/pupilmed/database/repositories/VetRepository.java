package com.example.pupilmed.database.repositories;

import com.example.pupilmed.database.models.user.User;
import com.example.pupilmed.database.models.vet.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VetRepository extends JpaRepository<Vet,Long> {

//    @Query("SELECT v FROM Vet v WHERE v.clinicAddress = ?1")
    Optional<Vet> findVetByClinicAddress(String clinicAddress);

    Vet getVetByUser_Username(String username);
    boolean existsByUser(User user);

    Vet getVetByUser(User user);

}
