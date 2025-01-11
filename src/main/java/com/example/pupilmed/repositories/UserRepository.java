package com.example.pupilmed.repositories;

import com.example.pupilmed.models.database.Role;
import com.example.pupilmed.models.database.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndRole(String username, Role role);
    boolean existsByUsername(String username);
    boolean existsByRoleAndId(Role role, Integer id);
    Optional<User> findById(Integer id);
}
