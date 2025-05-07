package com.example.dev.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest
{
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}
