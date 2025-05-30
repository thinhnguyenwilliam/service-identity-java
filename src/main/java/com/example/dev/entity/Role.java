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
public class Role {

  @Id String name;

  String description;

  @ManyToMany(mappedBy = "roles")
  Set<User> users;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_permissions",
      joinColumns = @JoinColumn(name = "role_name"),
      inverseJoinColumns = @JoinColumn(name = "permission_name"))
  Set<Permission> permissions;
}
