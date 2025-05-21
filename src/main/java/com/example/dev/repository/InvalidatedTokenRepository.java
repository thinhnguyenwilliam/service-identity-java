package com.example.dev.repository;

import com.example.dev.entity.InvalidatedToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String>{
    void deleteByExpiryTimeBefore(Date date);
}
