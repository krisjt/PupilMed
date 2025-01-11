package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.Owner;
import com.example.pupilmed.models.database.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Integer> {

    Owner findOwnerByUser(User user);
    Owner findOwnerByUser_Username(String username);
    boolean existsByUser(User user);
}
