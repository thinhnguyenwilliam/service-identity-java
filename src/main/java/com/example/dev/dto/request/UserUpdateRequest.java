package com.example.dev.dto.request;

import com.example.dev.validation.DobConstraint;
import java.time.LocalDate;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  String password;
  String firstName;
  String lastName;

  @DobConstraint(min = 13, message = "INVALID_DOB")
  LocalDate dob;

  List<String> roles;
}
