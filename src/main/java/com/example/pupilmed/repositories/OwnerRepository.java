package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.owner.Owner;
import com.example.pupilmed.models.database.user.User;
import com.example.pupilmed.models.database.vet.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Long> {

    Owner findOwnerByUser(User user);
    Owner findOwnerByUser_Username(String username);
    boolean existsByUser(User user);
    Owner getOwnerByUser(User user);
}
