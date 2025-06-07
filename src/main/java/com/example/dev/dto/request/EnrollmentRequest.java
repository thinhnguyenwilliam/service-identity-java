package com.example.dev.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentRequest {

    private Long studentId;
    private Long courseId;
    private String grade;

    // Getters and Setters
}

