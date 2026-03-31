package com.boutique;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RegistrationRequest(
    @NotBlank(message = "Email is required") 
    @Email(message = "Invalid email format") 
    String email,

    @NotBlank(message = "Password is required") 
    String password,

    @NotBlank(message = "Full name is required") 
    String fullName,

    @NotBlank(message = "Mobile number is required") 
    String mobileNumber,

    @NotNull(message = "Date of birth is required") 
    LocalDate dob
) {}