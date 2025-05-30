package com.example.dev.dto.response;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  String id;
  String username;
  String firstName;
  String lastName;
  String dob;
  Set<RoleResponse> roles;
}
