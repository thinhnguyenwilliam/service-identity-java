package com.example.dev.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
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
public class User extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String username;
  String password;
  String firstName;
  String lastName;
  LocalDate dob;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_name"))
  Set<Role> roles;
}
