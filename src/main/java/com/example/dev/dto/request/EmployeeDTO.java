package com.example.dev.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    private Long id;
    private String name;
    private String role;
    private Long departmentId; // 👈 needed to assign employee to department
}

