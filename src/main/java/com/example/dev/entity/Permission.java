package com.example.dev.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {

  @Id String name;

  String description;

  @ManyToMany(mappedBy = "permissions")
  Set<Role> roles;
}
