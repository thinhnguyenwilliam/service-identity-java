package com.example.dev.dto.request;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {
    private Long id;
    private String name;
    private List<EmployeeDTO> employees;  // one-to-many
}
