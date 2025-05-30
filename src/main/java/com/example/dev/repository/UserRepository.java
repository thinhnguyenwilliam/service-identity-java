package com.example.dev.repository;

import com.example.dev.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);
}
