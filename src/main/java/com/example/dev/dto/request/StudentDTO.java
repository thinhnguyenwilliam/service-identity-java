package com.example.dev.dto.request;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String name;
    private String email;

    private List<CourseDTO> courses;
    // Getters and setters
}

