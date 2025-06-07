package com.example.dev.repository;

import com.example.dev.entity.Enrollment;
import com.example.dev.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {}
