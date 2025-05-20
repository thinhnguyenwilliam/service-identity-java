package com.example.dev.dto.request;

import com.example.dev.validation.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3, message = "USER_INVALID")
    String username;


    @Size(min = 9, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "First name is required")
    String firstName;

    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;


}
