package com.example.pupilmed.database.repositories;

import com.example.pupilmed.database.models.owner.Owner;
import com.example.pupilmed.database.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Long> {

    Owner findOwnerByUser(User user);
    Owner findOwnerByUser_Username(String username);
    boolean existsByUser(User user);


}
