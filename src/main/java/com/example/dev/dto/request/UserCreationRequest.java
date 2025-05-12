package com.example.dev.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreationRequest {

    @Size(min = 3, message = "USER_INVALIDs")
    private String username;


    @Size(min = 6, message = "PASSWORD_INVALIDs")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    private LocalDate dob;

}
