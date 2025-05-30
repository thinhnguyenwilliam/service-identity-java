package com.example.dev.repository;

import com.example.dev.entity.InvalidatedToken;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
  void deleteByExpiryTimeBefore(Date date);
}
