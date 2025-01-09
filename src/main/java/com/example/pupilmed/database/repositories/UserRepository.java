package com.example.pupilmed.database.repositories;

import com.example.pupilmed.database.models.user.Role;
import com.example.pupilmed.database.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndRole(String username, Role role);
}
