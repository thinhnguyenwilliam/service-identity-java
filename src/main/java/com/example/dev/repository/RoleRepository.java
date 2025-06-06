package com.example.dev.repository;

import com.example.dev.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String userRole);

    Optional<Role> findByName(String adminRole);
}
