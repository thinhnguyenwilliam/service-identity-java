package com.example.dev.dto.request;

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


    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "First name is required")
    String firstName;

    String lastName;

    LocalDate dob;

}
