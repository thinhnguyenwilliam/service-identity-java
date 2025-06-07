package com.example.dev.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "employees")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Employee {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
